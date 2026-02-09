package com.kdgm.lumagallery.ui.screens.gallery.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kdgm.lumagallery.ui.screens.gallery.model.GalleryImage
import com.kdgm.lumagallery.ui.theme.PhotoBorderGray

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StickyGroupedPhotoGrid(
    groupedImages: Map<String, List<GalleryImage>>,
    allImages: List<GalleryImage>,
    selectedIds: Set<Long>,
    onTap: (GalleryImage) -> Unit,
    onLongPress: (GalleryImage) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        groupedImages.forEach { (date, images) ->

            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = date,
                        color = Color.White
                    )
                }
            }

            items(images.chunked(3)) { rowImages ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowImages.forEach { image ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .border(
                                    width = 1.dp,
                                    color = if (selectedIds.contains(image.id))
                                        Color.White
                                    else
                                        PhotoBorderGray
                                )
                                .combinedClickable(
                                    onClick = { onTap(image) },
                                    onLongClick = { onLongPress(image) }
                                )
                        ) {
                            AsyncImage(
                                model = image.uri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            SelectionOverlay(
                                selected = selectedIds.contains(image.id),
                                isSelectionMode = selectedIds.isNotEmpty()
                            )
                        }
                    }

                    repeat(3 - rowImages.size) {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        )
                    }
                }
            }
        }
    }
}
