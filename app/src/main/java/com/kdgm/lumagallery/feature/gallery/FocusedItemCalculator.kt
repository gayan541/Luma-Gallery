package com.kdgm.lumagallery.feature.gallery

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.ui.geometry.Offset
import com.kdgm.lumagallery.core.media.ImageMedia

fun calculateFocusedIndex(
    gridState: LazyGridState,
    images: List<ImageMedia>,
    focalOffset: Offset
): Int {

    val visibleItems = gridState.layoutInfo.visibleItemsInfo

    val focusedItem = visibleItems.firstOrNull { item ->
        val top = item.offset.y
        val bottom = top + item.size.height
        focalOffset.y in top.toFloat()..bottom.toFloat()
    }

    return focusedItem?.index ?: gridState.firstVisibleItemIndex
}
