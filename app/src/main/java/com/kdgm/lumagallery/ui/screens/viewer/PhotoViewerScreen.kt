package com.kdgm.lumagallery.ui.screens.viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
import com.kdgm.lumagallery.ui.screens.gallery.box.ThumbnailStrip
import com.kdgm.lumagallery.ui.screens.gallery.box.ZoomableImage
import com.kdgm.lumagallery.ui.screens.gallery.GalleryViewModel
import com.kdgm.lumagallery.ui.screens.gallery.box.ViewerBottomActions
import com.kdgm.lumagallery.ui.screens.gallery.box.ViewerTopBar

@Composable
fun PhotoViewerScreen(
    startIndex: Int,
    viewModel: GalleryViewModel,
    onBack: () -> Unit
) {
    val images by viewModel.images.collectAsState()

    val pagerState = rememberPagerState(
        initialPage = startIndex,
        pageCount = { images.size }
    )

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        // MAIN IMAGE
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            ZoomableImage(images[page].uri)
        }

        // TOP BAR
        ViewerTopBar(onBack = onBack)

        // BOTTOM AREA
        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            ThumbnailStrip(
                images = images,
                selectedIndex = pagerState.currentPage,
                onThumbClick = { index ->
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )

            ViewerBottomActions()
        }
    }
}
