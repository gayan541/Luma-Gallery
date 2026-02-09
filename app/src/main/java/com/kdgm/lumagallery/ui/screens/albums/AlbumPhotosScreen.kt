package com.kdgm.lumagallery.ui.screens.albums

import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kdgm.lumagallery.ui.screens.gallery.model.GalleryImage

@Composable
fun AlbumPhotosScreen(
    bucketId: Long,
    albumName: String,
    navController: NavController
) {
    val context = LocalContext.current
    var photos by remember { mutableStateOf<List<GalleryImage>>(emptyList()) }

    LaunchedEffect(bucketId) {
        photos = loadAlbumPhotos(context, bucketId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = albumName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Text(
                    text = "${photos.size} items",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }

        // Photo Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(photos) { photo ->
                AsyncImage(
                    model = photo.uri,
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clickable {
                            // Navigate to viewer
                            // You can implement this by passing photos to ViewModel
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
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
