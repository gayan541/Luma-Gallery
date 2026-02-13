package com.kdgm.lumagallery.ui.screens.viewer

import android.content.Intent
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
    val context = LocalContext.current

    val pagerState = rememberPagerState(
        initialPage = startIndex.coerceIn(0, (images.size - 1).coerceAtLeast(0)),
        pageCount = { images.size }
    )

    val scope = rememberCoroutineScope()
    var uiVisible by remember { mutableStateOf(true) }
    var showInfoDialog by remember { mutableStateOf(false) }
    var isMenuOpen by remember { mutableStateOf(false) }

    // Clear selection IMMEDIATELY when entering viewer
    LaunchedEffect(Unit) {
        viewModel.clearSelection()
    }

    // Auto-hide UI after 3 seconds - but NOT if menu is open or info dialog is showing
    LaunchedEffect(uiVisible, isMenuOpen, showInfoDialog) {
        if (uiVisible && !isMenuOpen && !showInfoDialog) {
            delay(3000)
            if (!isMenuOpen && !showInfoDialog) {
                uiVisible = false
            }
        }
    }

    // If all images are deleted, go back
    LaunchedEffect(images.size) {
        if (images.isEmpty()) {
            onBack()
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
                            if (!isMenuOpen) {
                                uiVisible = !uiVisible
                            }
                        }
                    )
                }
        ) { page ->
            if (page < images.size) {
                ZoomableImage(
                    uri = images[page].uri,
                    onTap = {
                        if (!isMenuOpen) {
                            uiVisible = !uiVisible
                        }
                    }
                )
            }
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
                onInfo = {
                    showInfoDialog = true
                },
                onRotateLeft = {
                    Toast.makeText(context, "Rotate left (coming soon)", Toast.LENGTH_SHORT).show()
                },
                onRotateRight = {
                    Toast.makeText(context, "Rotate right (coming soon)", Toast.LENGTH_SHORT).show()
                },
                onSetAsWallpaper = {
                    images.getOrNull(pagerState.currentPage)?.let { image ->
                        setAsWallpaper(context, image.uri)
                    }
                },
                onPrint = {
                    Toast.makeText(context, "Print (coming soon)", Toast.LENGTH_SHORT).show()
                },
                onMenuOpenChange = { isOpen ->
                    isMenuOpen = isOpen
                },
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
                    currentImage = images.getOrNull(pagerState.currentPage),
                    viewModel = viewModel,
                    onDeleteComplete = { success ->
                        // Don't automatically go back - stay in viewer
                        // unless it was the last photo
                        if (success && images.size <= 1) {
                            onBack()
                        }
                    }
                )
            }
        }
    }

    // INFO DIALOG
    if (showInfoDialog) {
        images.getOrNull(pagerState.currentPage)?.let { image ->
            PhotoInfoDialog(
                uri = image.uri,
                context = context,
                onDismiss = { showInfoDialog = false }
            )
        }
    }
}

private fun setAsWallpaper(context: android.content.Context, uri: android.net.Uri) {
    try {
        val intent = Intent(Intent.ACTION_ATTACH_DATA).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            setDataAndType(uri, "image/*")
            putExtra("mimeType", "image/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Set as wallpaper"))
    } catch (e: Exception) {
        Toast.makeText(context, "Failed to set wallpaper", Toast.LENGTH_SHORT).show()
    }
}
