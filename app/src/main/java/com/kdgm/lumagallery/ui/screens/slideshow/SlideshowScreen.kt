package com.kdgm.lumagallery.ui.screens.slideshow

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.kdgm.lumagallery.ui.screens.gallery.GalleryViewModel
import kotlinx.coroutines.delay
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kdgm.lumagallery.ui.screens.gallery.box.ThumbnailStrip

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

    val scope = rememberCoroutineScope()

    // 🔁 Auto slideshow (3s)
    LaunchedEffect(playing, index) {
        if (playing && images.isNotEmpty()) {
            delay(3000)
            index = (index + 1) % images.size
        }
    }

    // ⏳ Auto-hide controls
    LaunchedEffect(controlsVisible) {
        if (controlsVisible) {
            delay(2500)
            controlsVisible = false
        }
    }

    // 🚀 Preload next & previous images
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
            .pointerInput(Unit) {
                detectTapGestures {
                    controlsVisible = true
                }
            }
    ) {

        // 🎞 SMOOTH CROSSFADE CONTENT (NO BLACK FRAMES)
        AnimatedContent(
            targetState = index,
            transitionSpec = {
                fadeIn(animationSpec = tween(600)) togetherWith
                        fadeOut(animationSpec = tween(600))
            },
            modifier = Modifier.fillMaxSize(),
            label = "slideshow"
        ) { page ->
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(images[page].uri)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // 🔝 TOP BAR
        AnimatedVisibility(
            visible = controlsVisible,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            IconButton(
                onClick = onExit,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        // ⏯ CONTROLS
        AnimatedVisibility(
            visible = controlsVisible,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 96.dp)
            ) {
                Text(
                    text = "${index + 1} / ${images.size}",
                    color = Color.White
                )

                IconButton(onClick = { playing = !playing }) {
                    Icon(
                        imageVector = if (playing)
                            Icons.Outlined.Pause
                        else
                            Icons.Outlined.PlayArrow,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }

        // 🧱 THUMBNAILS
        AnimatedVisibility(
            visible = controlsVisible,
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


