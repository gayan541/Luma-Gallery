package com.kdgm.lumagallery.ui.screens.viewer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.kdgm.lumagallery.ui.screens.gallery.GalleryViewModel
import com.kdgm.lumagallery.ui.screens.gallery.box.ThumbnailStrip
import com.kdgm.lumagallery.ui.screens.gallery.box.ViewerBottomActions
import com.kdgm.lumagallery.ui.screens.gallery.box.ViewerTopBar
import com.kdgm.lumagallery.ui.screens.gallery.box.ZoomableImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PhotoViewerScreen(
    startIndex: Int,
    viewModel: GalleryViewModel,
    onBack: () -> Unit,
    onStartSlideshow: () -> Unit
) {
    val images by viewModel.images.collectAsState()

    val pagerState = rememberPagerState(
        initialPage = startIndex,
        pageCount = { images.size }
    )

    val scope = rememberCoroutineScope()
    var uiVisible by remember { mutableStateOf(true) }

    // Auto-hide UI after 3 seconds
    LaunchedEffect(uiVisible) {
        if (uiVisible) {
            delay(3000)
            uiVisible = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures {
                    uiVisible = !uiVisible
                }
            }
    ) {

        // MAIN IMAGE PAGER
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            ZoomableImage(images[page].uri)
        }

        // TOP BAR
        AnimatedVisibility(
            visible = uiVisible,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            ViewerTopBar(
                onBack = onBack,
                onSlideshow = onStartSlideshow
            )
        }

        // BOTTOM AREA
        AnimatedVisibility(
            visible = uiVisible,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column {
                ThumbnailStrip(
                    images = images,
                    selectedIndex = pagerState.currentPage,
                    onThumbClick = { index ->
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )

                ViewerBottomActions(
                    currentImage = images.getOrNull(pagerState.currentPage)
                )
            }
        }
    }
}
