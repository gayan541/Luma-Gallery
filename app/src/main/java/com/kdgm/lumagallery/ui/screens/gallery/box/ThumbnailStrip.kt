package com.kdgm.lumagallery.ui.screens.gallery.box

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kdgm.lumagallery.ui.screens.gallery.model.GalleryImage

@Composable
fun ThumbnailStrip(
    images: List<GalleryImage>,
    selectedIndex: Int,
    onThumbClick: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        itemsIndexed(images) { index, image ->
            AsyncImage(
                model = image.uri,
                contentDescription = null,
                modifier = Modifier
                    .size(if (index == selectedIndex) 56.dp else 48.dp)
                    .border(
                        width = if (index == selectedIndex) 2.dp else 0.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickable {
                        onThumbClick(index)   // ✅ SAFE
                    },
                contentScale = ContentScale.Crop
            )
        }
    }
}



