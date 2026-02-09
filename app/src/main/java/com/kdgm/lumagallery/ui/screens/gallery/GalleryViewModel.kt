package com.kdgm.lumagallery.ui.screens.gallery

import android.Manifest
import android.app.Application
import android.app.RecoverableSecurityException
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.IntentSenderRequest
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kdgm.lumagallery.ui.screens.gallery.model.GalleryImage
import com.kdgm.lumagallery.ui.screens.gallery.util.getDateLabel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    /* ---------------- Permission ---------------- */

    private val _permissionState =
        MutableStateFlow<GalleryPermissionState>(GalleryPermissionState.Checking)
    val permissionState = _permissionState.asStateFlow()

    fun checkPermission(context: Context) {
        val permission = if (Build.VERSION.SDK_INT >= 33)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

        val granted = ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            _permissionState.value = GalleryPermissionState.Granted
            loadImages()
        } else {
            _permissionState.value = GalleryPermissionState.Denied
        }
    }

    /* ---------------- Images ---------------- */

    private val _images = MutableStateFlow<List<GalleryImage>>(emptyList())
    val images = _images.asStateFlow()

    val groupedImages = images.map { list ->
        list.groupBy { getDateLabel(it.dateTaken) }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyMap()
    )

    fun loadImages() = viewModelScope.launch {
        val resolver = getApplication<Application>().contentResolver
        val result = mutableListOf<GalleryImage>()

        resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_TAKEN
            ),
            null,
            null,
            "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val dateCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val date = cursor.getLong(dateCol)

                val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    .buildUpon()
                    .appendPath(id.toString())
                    .build()

                result.add(GalleryImage(id, uri, date))
            }
        }

        _images.value = result
    }

    /* ---------------- Selection ---------------- */

    private val _selectedIds = MutableStateFlow<Set<Long>>(emptySet())
    val selectedIds = _selectedIds.asStateFlow()

    val isSelectionMode = selectedIds.map { it.isNotEmpty() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun toggleSelection(image: GalleryImage) {
        _selectedIds.update { current ->
            if (current.contains(image.id)) current - image.id
            else current + image.id
        }
    }

    fun clearSelection() {
        _selectedIds.value = emptySet()
    }

    fun selectAll(images: List<GalleryImage>) {
        _selectedIds.value = images.map { it.id }.toSet()
    }

    /* ---------------- Delete ---------------- */

    // Store pending delete intent sender
    private val _pendingDeleteIntentSender = MutableStateFlow<IntentSender?>(null)
    val pendingDeleteIntentSender = _pendingDeleteIntentSender.asStateFlow()

    fun deleteSelectedImages(context: Context, onNeedPermission: (IntentSender) -> Unit, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val selectedImages = _images.value.filter {
                    _selectedIds.value.contains(it.id)
                }

                if (selectedImages.isEmpty()) {
                    onComplete(false)
                    return@launch
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    // Android 11+ - Use createDeleteRequest
                    deleteImagesModern(context, selectedImages, onNeedPermission, onComplete)
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Android 10 - Handle RecoverableSecurityException
                    deleteImagesAndroid10(context, selectedImages, onNeedPermission, onComplete)
                } else {
                    // Android 9 and below - Direct delete
                    deleteImagesLegacy(context, selectedImages, onComplete)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onComplete(false)
            }
        }
    }

    // Android 11+ (API 30+)
    private suspend fun deleteImagesModern(
        context: Context,
        images: List<GalleryImage>,
        onNeedPermission: (IntentSender) -> Unit,
        onComplete: (Boolean) -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val uris = images.map { it.uri }
            val pendingIntent = MediaStore.createDeleteRequest(
                context.contentResolver,
                uris
            )

            // This will trigger the system permission dialog
            onNeedPermission(pendingIntent.intentSender)
        }
    }

    // Android 10 (API 29)
    private suspend fun deleteImagesAndroid10(
        context: Context,
        images: List<GalleryImage>,
        onNeedPermission: (IntentSender) -> Unit,
        onComplete: (Boolean) -> Unit
    ) {
        try {
            val resolver = context.contentResolver
            var deletedCount = 0

            images.forEach { image ->
                try {
                    val deleted = resolver.delete(image.uri, null, null)
                    if (deleted > 0) deletedCount++
                } catch (securityException: SecurityException) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val recoverableException = securityException as? RecoverableSecurityException
                        recoverableException?.let {
                            onNeedPermission(it.userAction.actionIntent.intentSender)
                            return
                        }
                    }
                    throw securityException
                }
            }

            if (deletedCount > 0) {
                clearSelection()
                loadImages()
            }
            onComplete(deletedCount > 0)
        } catch (e: Exception) {
            e.printStackTrace()
            onComplete(false)
        }
    }

    // Android 9 and below (API 28-)
    private suspend fun deleteImagesLegacy(
        context: Context,
        images: List<GalleryImage>,
        onComplete: (Boolean) -> Unit
    ) {
        val resolver = context.contentResolver
        var deletedCount = 0

        images.forEach { image ->
            val deleted = resolver.delete(image.uri, null, null)
            if (deleted > 0) deletedCount++
        }

        if (deletedCount > 0) {
            clearSelection()
            loadImages()
        }
        onComplete(deletedCount > 0)
    }

    // Called after user grants permission
    fun onDeletePermissionGranted() {
        viewModelScope.launch {
            clearSelection()
            loadImages()
        }
    }

    /* ---------------- Share ---------------- */

    fun getSelectedImages(): List<GalleryImage> {
        return _images.value.filter { _selectedIds.value.contains(it.id) }
    }
}
