package com.kdgm.lumagallery.ui.screens.albums

import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kdgm.lumagallery.ui.screens.gallery.components.GalleryBottomBar
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.ui.Alignment

data class AlbumUiModel(
    val bucketId: Long,
    val name: String,
    val count: Int,
    val coverUri: Uri
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumsScreen(
    navController: NavController
) {
    val context = LocalContext.current

    var albums by remember { mutableStateOf<List<AlbumUiModel>>(emptyList()) }
    var selectedAlbums by remember { mutableStateOf<Set<Long>>(emptySet()) }
    var showCreateDialog by remember { mutableStateOf(false) }

    val isSelectionMode = selectedAlbums.isNotEmpty()

    LaunchedEffect(Unit) {
        albums = loadAlbums(context)
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            if (isSelectionMode) {
                AlbumSelectionTopBar(
                    selectedCount = selectedAlbums.size,
                    onCancel = { selectedAlbums = emptySet() },
                    onDelete = {
                        // TODO: Delete selected albums
                        selectedAlbums = emptySet()
                    }
                )
            }
        },
        bottomBar = {
            if (!isSelectionMode) {
                GalleryBottomBar(
                    navController = navController,
                    currentRoute = "albums"
                )
            }
        },
        floatingActionButton = {
            if (!isSelectionMode) {
                FloatingActionButton(
                    onClick = { showCreateDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Create Album")
                }
            }
        }
    ) { padding ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(albums) { album ->
                AlbumItem(
                    album = album,
                    isSelected = selectedAlbums.contains(album.bucketId),
                    isSelectionMode = isSelectionMode,
                    onClick = {
                        if (isSelectionMode) {
                            selectedAlbums = if (selectedAlbums.contains(album.bucketId)) {
                                selectedAlbums - album.bucketId
                            } else {
                                selectedAlbums + album.bucketId
                            }
                        } else {
                            val encodedName = URLEncoder.encode(
                                album.name,
                                StandardCharsets.UTF_8.toString()
                            )
                            navController.navigate("album/${album.bucketId}/$encodedName")
                        }
                    },
                    onLongClick = {
                        if (!isSelectionMode) {
                            selectedAlbums = setOf(album.bucketId)
                        }
                    }
                )
            }
        }
    }

    if (showCreateDialog) {
        CreateAlbumDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { albumName ->
                // TODO: Create new album
                showCreateDialog = false
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AlbumItem(
    album: AlbumUiModel,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Box {
        Column(
            modifier = Modifier
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick
                )
        ) {
            Box {
                AsyncImage(
                    model = album.coverUri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )

                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(16.dp))
                            .then(
                                Modifier.fillMaxSize()
                                    .padding(8.dp)
                            ),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = album.name,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${album.count} items",
                color = Color.Gray,
                fontSize = 13.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlbumSelectionTopBar(
    selectedCount: Int,
    onCancel: () -> Unit,
    onDelete: () -> Unit
) {
    TopAppBar(
        title = { Text("$selectedCount selected", color = Color.White) },
        navigationIcon = {
            TextButton(onClick = onCancel) {
                Text("Cancel", color = Color.White)
            }
        },
        actions = {
            TextButton(onClick = onDelete) {
                Text("Delete", color = Color.Red)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black
        )
    )
}

@Composable
private fun CreateAlbumDialog(
    onDismiss: () -> Unit,
    onCreate: (String) -> Unit
) {
    var albumName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Album") },
        text = {
            OutlinedTextField(
                value = albumName,
                onValueChange = { albumName = it },
                label = { Text("Album name") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (albumName.isNotBlank()) {
                        onCreate(albumName.trim())
                    }
                },
                enabled = albumName.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun loadAlbums(context: android.content.Context): List<AlbumUiModel> {
    val map = linkedMapOf<Long, AlbumUiModel>()
    val resolver = context.contentResolver

    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.BUCKET_ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    )

    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

    resolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        sortOrder
    )?.use { cursor ->

        val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val bucketIdCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
        val bucketNameCol =
            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

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


