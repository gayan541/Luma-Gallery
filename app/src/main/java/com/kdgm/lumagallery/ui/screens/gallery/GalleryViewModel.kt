package com.kdgm.lumagallery.ui.screens.gallery

import android.Manifest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kdgm.lumagallery.ui.screens.gallery.model.GalleryImage
import com.kdgm.lumagallery.ui.screens.gallery.util.getDateLabel
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

    private fun loadImages() = viewModelScope.launch {
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
}
