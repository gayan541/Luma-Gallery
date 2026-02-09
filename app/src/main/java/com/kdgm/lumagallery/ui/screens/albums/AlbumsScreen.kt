package com.kdgm.lumagallery.ui.screens.albums

import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.clickable
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

data class AlbumUiModel(
    val bucketId: Long,
    val name: String,
    val count: Int,
    val coverUri: Uri
)

@Composable
fun AlbumsScreen(
    navController: NavController
) {
    val context = LocalContext.current

    var albums by remember {
        mutableStateOf<List<AlbumUiModel>>(emptyList())
    }

    LaunchedEffect(Unit) {
        albums = loadAlbums(context)
    }

    Scaffold(
        containerColor = Color.Black,
        bottomBar = {
            GalleryBottomBar(
                navController = navController,
                currentRoute = "albums"
            )
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
                Column(
                    modifier = Modifier
                        .clickable {
                            val encodedName = URLEncoder.encode(
                                album.name,
                                StandardCharsets.UTF_8.toString()
                            )
                            navController.navigate("album/${album.bucketId}/$encodedName")
                        }
                ) {
                    AsyncImage(
                        model = album.coverUri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )

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
    }
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


