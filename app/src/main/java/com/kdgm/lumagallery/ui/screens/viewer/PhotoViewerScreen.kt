package com.kdgm.lumagallery.ui.screens.viewer

import androidx.compose.ui.unit.dp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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

    // Auto-hide UI after 3 seconds when visible
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
            .systemBarsPadding()
    ) {

        // MAIN IMAGE PAGER with tap gesture
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            // Toggle UI visibility on tap
                            uiVisible = !uiVisible
                        }
                    )
                }
        ) { page ->
            // Pass the uiVisible state to ZoomableImage so it knows when NOT to intercept taps
            ZoomableImage(
                uri = images[page].uri,
                onTap = {
                    // This will be called when image is not zoomed
                    uiVisible = !uiVisible
                }
            )
        }

        // TOP GRADIENT OVERLAY
        AnimatedVisibility(
            visible = uiVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.7f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }

        // TOP BAR
        AnimatedVisibility(
            visible = uiVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            ViewerTopBar(
                onBack = onBack,
                onSlideshow = onStartSlideshow,
                currentIndex = pagerState.currentPage,
                totalCount = images.size
            )
        }

        // BOTTOM GRADIENT OVERLAY
        AnimatedVisibility(
            visible = uiVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    )
            )
        }

        // BOTTOM CONTENT
        AnimatedVisibility(
            visible = uiVisible,
            enter = fadeIn(),
            exit = fadeOut(),
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
