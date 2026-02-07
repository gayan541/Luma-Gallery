package com.kdgm.lumagallery.feature.gallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.kdgm.lumagallery.core.media.ImageMedia

@Composable
fun GalleryGridItem(
    image: ImageMedia,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    AsyncImage(
        model = image.uri,
        contentDescription = null,
        modifier = modifier
            .aspectRatio(1f)
            .clickable { onClick() }
            .fillMaxSize()
    )
}

