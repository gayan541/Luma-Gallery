package com.kdgm.lumagallery.feature.gallery

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kdgm.lumagallery.core.media.ImageMedia
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private sealed interface GalleryUiItem {
    data class Header(val title: String) : GalleryUiItem
    data class Photo(
        val image: ImageMedia,
        val index: Int
    ) : GalleryUiItem
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GalleryScreen(
    viewModel: GalleryViewModel,
    onImageOpen: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val uiItems = remember(state.images) {
        buildUiItems(state.images)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 72.dp),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {

        items(
            count = uiItems.size,
            span = { index ->
                when (uiItems[index]) {
                    is GalleryUiItem.Header ->
                        GridItemSpan(maxLineSpan)
                    is GalleryUiItem.Photo ->
                        GridItemSpan(1)
                }
            }
        ) { index ->

            when (val item = uiItems[index]) {

                is GalleryUiItem.Header -> {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }

                is GalleryUiItem.Photo -> {
                    GalleryGridItem(
                        image = item.image,
                        modifier = Modifier.aspectRatio(1f),
                        onClick = {
                            onImageOpen(item.index)
                        }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun buildUiItems(
    images: List<ImageMedia>
): List<GalleryUiItem> {

    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    val formatter =
        DateTimeFormatter.ofPattern("MMM d, yyyy")

    val grouped =
        images.groupBy { image ->
            Instant
                .ofEpochMilli(image.dateTaken)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }

    val sortedDates =
        grouped.keys.sortedDescending()

    val items = mutableListOf<GalleryUiItem>()
    var globalIndex = 0

    for (date in sortedDates) {

        val title =
            when (date) {
                today -> "Today"
                yesterday -> "Yesterday"
                else -> date.format(formatter)
            }

        items += GalleryUiItem.Header(title)

        grouped[date]?.forEach { image ->
            items += GalleryUiItem.Photo(
                image = image,
                index = globalIndex
            )
            globalIndex++
        }
    }

    return items
}


