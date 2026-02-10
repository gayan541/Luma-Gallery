package com.kdgm.lumagallery.ui.screens.slideshow

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kdgm.lumagallery.ui.screens.gallery.GalleryViewModel
import com.kdgm.lumagallery.ui.screens.gallery.box.ThumbnailStrip
import kotlinx.coroutines.delay

@Composable
fun SlideshowScreen(
    viewModel: GalleryViewModel,
    onExit: () -> Unit
) {
    val images by viewModel.images.collectAsState()
    val context = LocalContext.current
    val imageLoader = remember { ImageLoader(context) }

    var index by remember { mutableStateOf(0) }
    var playing by remember { mutableStateOf(true) }
    var controlsVisible by remember { mutableStateOf(true) }

    // Auto slideshow (3s)
    LaunchedEffect(playing, index) {
        if (playing && images.isNotEmpty()) {
            delay(3000)
            index = (index + 1) % images.size
        }
    }

    // Auto-hide controls after 3 seconds when visible
    LaunchedEffect(controlsVisible) {
        if (controlsVisible) {
            delay(3000)
            controlsVisible = false
        }
    }

    // Preload next & previous images
    LaunchedEffect(index) {
        if (images.isEmpty()) return@LaunchedEffect

        listOf(index + 1, index - 1).forEach { i ->
            val safe = (i + images.size) % images.size
            imageLoader.enqueue(
                ImageRequest.Builder(context)
                    .data(images[safe].uri)
                    .size(coil.size.Size.ORIGINAL)
                    .build()
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding()
    ) {

        // SMOOTH CROSSFADE CONTENT with tap gesture
        AnimatedContent(
            targetState = index,
            transitionSpec = {
                fadeIn(animationSpec = tween(600)) togetherWith
                        fadeOut(animationSpec = tween(600))
            },
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            // Toggle controls visibility on tap
                            controlsVisible = !controlsVisible
                        }
                    )
                },
            label = "slideshow"
        ) { page ->
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(images[page].uri)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        // TOP GRADIENT OVERLAY
        AnimatedVisibility(
            visible = controlsVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
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
            visible = controlsVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onExit) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Exit",
                        tint = Color.White
                    )
                }

                Text(
                    text = "${index + 1} / ${images.size}",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }

        // BOTTOM GRADIENT OVERLAY
        AnimatedVisibility(
            visible = controlsVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
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

        // PLAY/PAUSE BUTTON
        AnimatedVisibility(
            visible = controlsVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 120.dp)
            ) {
                IconButton(
                    onClick = { playing = !playing },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = if (playing)
                            Icons.Outlined.Pause
                        else
                            Icons.Outlined.PlayArrow,
                        contentDescription = if (playing) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        // THUMBNAILS
        AnimatedVisibility(
            visible = controlsVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            ThumbnailStrip(
                images = images,
                selectedIndex = index,
                onThumbClick = { i ->
                    playing = false
                    controlsVisible = true
                    index = i
                }
            )
        }
    }
}


