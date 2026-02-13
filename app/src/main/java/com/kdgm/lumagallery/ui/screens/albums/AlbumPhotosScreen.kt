package com.kdgm.lumagallery.ui.screens.albums

import android.provider.MediaStore
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Slideshow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kdgm.lumagallery.ui.screens.gallery.components.SelectionOverlay
import com.kdgm.lumagallery.ui.screens.gallery.model.GalleryImage
import com.kdgm.lumagallery.ui.theme.PhotoBorderGray

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AlbumPhotosScreen(
    bucketId: Long,
    albumName: String,
    navController: NavController
) {
    val context = LocalContext.current
    var photos by remember { mutableStateOf<List<GalleryImage>>(emptyList()) }
    var selectedPhotos by remember { mutableStateOf<Set<Long>>(emptySet()) }
    var showMoveDialog by remember { mutableStateOf(false) }
    var showCopyDialog by remember { mutableStateOf(false) }
    var showOptionsMenu by remember { mutableStateOf(false) }

    val isSelectionMode = selectedPhotos.isNotEmpty()

    LaunchedEffect(bucketId) {
        photos = loadAlbumPhotos(context, bucketId)
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            if (isSelectionMode) {
                AlbumPhotoSelectionTopBar(
                    selectedCount = selectedPhotos.size,
                    onCancel = { selectedPhotos = emptySet() },
                    onSelectAll = {
                        selectedPhotos = if (selectedPhotos.size == photos.size) {
                            emptySet()
                        } else {
                            photos.map { it.id }.toSet()
                        }
                    }
                )
            } else {
                AlbumPhotosTopBar(
                    albumName = albumName,
                    photoCount = photos.size,
                    onBack = { navController.popBackStack() },
                    onSlideshow = {
                        // TODO: Start slideshow with album photos
                    },
                    onMoreClick = { showOptionsMenu = true }
                )
            }
        },
        bottomBar = {
            if (isSelectionMode) {
                AlbumPhotoSelectionBottomBar(
                    onMove = { showMoveDialog = true },
                    onCopy = { showCopyDialog = true },
                    onRemove = {
                        // TODO: Remove from album
                        selectedPhotos = emptySet()
                    }
                )
            }
        }
    ) { padding ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(photos) { photo ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .border(
                            width = 1.dp,
                            color = if (selectedPhotos.contains(photo.id))
                                Color.White
                            else
                                PhotoBorderGray
                        )
                        .combinedClickable(
                            onClick = {
                                if (isSelectionMode) {
                                    selectedPhotos = if (selectedPhotos.contains(photo.id)) {
                                        selectedPhotos - photo.id
                                    } else {
                                        selectedPhotos + photo.id
                                    }
                                } else {
                                    // TODO: Navigate to viewer with album photos
                                }
                            },
                            onLongClick = {
                                if (!isSelectionMode) {
                                    selectedPhotos = setOf(photo.id)
                                }
                            }
                        )
                ) {
                    AsyncImage(
                        model = photo.uri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    SelectionOverlay(
                        selected = selectedPhotos.contains(photo.id),
                        isSelectionMode = isSelectionMode
                    )
                }
            }
        }
    }

    // Move Dialog
    if (showMoveDialog) {
        AlbumPickerDialog(
            title = "Move to Album",
            onDismiss = { showMoveDialog = false },
            onAlbumSelected = { targetAlbumId ->
                // TODO: Move photos to selected album
                showMoveDialog = false
                selectedPhotos = emptySet()
            }
        )
    }

    // Copy Dialog
    if (showCopyDialog) {
        AlbumPickerDialog(
            title = "Copy to Album",
            onDismiss = { showCopyDialog = false },
            onAlbumSelected = { targetAlbumId ->
                // TODO: Copy photos to selected album
                showCopyDialog = false
                selectedPhotos = emptySet()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlbumPhotosTopBar(
    albumName: String,
    photoCount: Int,
    onBack: () -> Unit,
    onSlideshow: () -> Unit,
    onMoreClick: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = albumName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Text(
                    text = "$photoCount items",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = onSlideshow) {
                Icon(
                    imageVector = Icons.Outlined.Slideshow,
                    contentDescription = "Slideshow",
                    tint = Color.White
                )
            }
            IconButton(onClick = onMoreClick) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "More",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlbumPhotoSelectionTopBar(
    selectedCount: Int,
    onCancel: () -> Unit,
    onSelectAll: () -> Unit
) {
    TopAppBar(
        title = { Text("$selectedCount selected", color = Color.White) },
        navigationIcon = {
            TextButton(onClick = onCancel) {
                Text("Cancel", color = Color.White)
            }
        },
        actions = {
            IconButton(onClick = onSelectAll) {
                Icon(
                    Icons.Outlined.CheckCircle,
                    contentDescription = "Select All",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black
        )
    )
}

@Composable
private fun AlbumPhotoSelectionBottomBar(
    onMove: () -> Unit,
    onCopy: () -> Unit,
    onRemove: () -> Unit
) {
    NavigationBar(
        containerColor = Color.Black,
        modifier = Modifier.navigationBarsPadding()
    ) {
        NavigationBarItem(
            selected = false,
            onClick = onMove,
            icon = { Icon(Icons.Outlined.ArrowBack, null) },
            label = { Text("Move") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onCopy,
            icon = { Icon(Icons.Outlined.CheckCircle, null) },
            label = { Text("Copy") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onRemove,
            icon = { Icon(Icons.Outlined.ArrowBack, null) },
            label = { Text("Remove") }
        )
    }
}

@Composable
private fun AlbumPickerDialog(
    title: String,
    onDismiss: () -> Unit,
    onAlbumSelected: (Long) -> Unit
) {
    val context = LocalContext.current
    var albums by remember { mutableStateOf<List<AlbumUiModel>>(emptyList()) }

    LaunchedEffect(Unit) {
        albums = loadAlbums(context)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                albums.forEach { album ->
                    TextButton(
                        onClick = { onAlbumSelected(album.bucketId) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "${album.name} (${album.count})",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun loadAlbumPhotos(
    context: android.content.Context,
    bucketId: Long
): List<GalleryImage> {
    val result = mutableListOf<GalleryImage>()
    val resolver = context.contentResolver

    resolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_TAKEN
        ),
        "${MediaStore.Images.Media.BUCKET_ID} = ?",
        arrayOf(bucketId.toString()),
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

    return result
}

private fun loadAlbums(context: android.content.Context): List<AlbumUiModel> {
    // Reuse the same function from AlbumsScreen
    val map = linkedMapOf<Long, AlbumUiModel>()
    val resolver = context.contentResolver

    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.BUCKET_ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    )

    resolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        null
    )?.use { cursor ->
        val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val bucketIdCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
        val bucketNameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        while (cursor.moveToNext()) {
            val imageId = cursor.getLong(idCol)
            val bucketId = cursor.getLong(bucketIdCol)
            val bucketName = cursor.getString(bucketNameCol) ?: "Unknown"

            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                .buildUpon()
                .appendPath(imageId.toString())
                .build()

            val existing = map[bucketId]
            if (existing == null) {
                map[bucketId] = AlbumUiModel(
                    bucketId = bucketId,
                    name = bucketName,
                    count = 1,
                    coverUri = uri
                )
            } else {
                map[bucketId] = existing.copy(count = existing.count + 1)
            }
        }
    }

    return map.values.toList()
}
