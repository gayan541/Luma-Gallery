package com.kdgm.lumagallery.feature.gallery

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.kdgm.lumagallery.core.media.ImageMedia

@Composable
fun GalleryGridItem(
    image: ImageMedia,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = image.uri,
        contentDescription = image.name,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxSize()
    )
}

