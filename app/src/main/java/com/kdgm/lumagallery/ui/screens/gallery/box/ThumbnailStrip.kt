package com.kdgm.lumagallery.ui.screens.gallery.box

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    val listState = rememberLazyListState()

    // Auto-scroll to selected item
    LaunchedEffect(selectedIndex) {
        if (selectedIndex in images.indices) {
            listState.animateScrollToItem(
                index = selectedIndex,
                scrollOffset = -100 // Center the item
            )
        }
    }

    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        itemsIndexed(images) { index, image ->
            val isSelected = index == selectedIndex

            Box(
                modifier = Modifier
                    .size(if (isSelected) 72.dp else 64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .then(
                        if (isSelected) {
                            Modifier.border(
                                width = 3.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                        } else {
                            Modifier.border(
                                width = 1.dp,
                                color = Color.White.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    )
                    .clickable { onThumbClick(index) }
            ) {
                AsyncImage(
                    model = image.uri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dim non-selected thumbnails
                if (!isSelected) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f))
                    )
                }
            }
        }
    }
}



