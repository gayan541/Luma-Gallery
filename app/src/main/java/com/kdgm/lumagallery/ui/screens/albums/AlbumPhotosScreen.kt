package com.kdgm.lumagallery.ui.screens.albums

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kdgm.lumagallery.ui.screens.gallery.GalleryViewModel

@Composable
fun AlbumPhotosScreen(
    albumName: String,
    viewModel: GalleryViewModel,
    onBack: () -> Unit
) {
    val images by viewModel.images.collectAsState()

    val albumImages = images.filter {
        extractAlbumName(it.uri) == albumName
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = albumName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(albumImages) { image ->
                AsyncImage(
                    model = image.uri,
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clickable {
                            // viewer navigation later
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

private fun extractAlbumName(uri: Any?): String {
    return try {
        val path = uri.toString()
        path.substringBeforeLast("/").substringAfterLast("/")
            .ifEmpty { "Unknown" }
    } catch (e: Exception) {
        "Unknown"
    }
}
