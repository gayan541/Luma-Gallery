package com.kdgm.lumagallery.feature.gallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.kdgm.lumagallery.data.datasource.MediaStoreImageDataSource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumImagesScreen(
    bucketId: Long,
    title: String,
    onBack: () -> Unit,
    onImageOpen: (Int) -> Unit
) {

    val context = LocalContext.current

    val images = remember {
        MediaStoreImageDataSource(context)
            .getImages()
            .filter { image ->
                image.uri.toString().contains(bucketId.toString())
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(images.size) { index ->
                GalleryGridItem(
                    image = images[index],
                    modifier = Modifier.clickable {
                        onImageOpen(index)
                    }
                )
            }
        }
    }


}
