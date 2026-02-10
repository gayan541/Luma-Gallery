package com.kdgm.lumagallery.ui.screens.gallery.box

import android.net.Uri
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun ZoomableImage(
    uri: Uri,
    onTap: () -> Unit = {}
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    // Tap gestures - only active when NOT zoomed
    val tapModifier = if (scale <= 1f) {
        Modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = {
                    // Single tap toggles UI
                    onTap()
                },
                onDoubleTap = {
                    // Double tap zooms in
                    scale = 2.5f
                }
            )
        }
    } else {
        Modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = {
                    // Single tap while zoomed also toggles UI
                    onTap()
                },
                onDoubleTap = {
                    // Double tap zooms out
                    scale = 1f
                    offset = Offset.Zero
                }
            )
        }
    }

    // Transform gestures - only active when zoomed
    val transformModifier = if (scale > 1f) {
        Modifier.pointerInput(Unit) {
            detectTransformGestures { _, pan, zoom, _ ->
                scale = (scale * zoom).coerceIn(1f, 5f)

                if (scale > 1f) {
                    val maxX = (size.width * (scale - 1)) / 2
                    val maxY = (size.height * (scale - 1)) / 2
                    offset = Offset(
                        x = (offset.x + pan.x).coerceIn(-maxX, maxX),
                        y = (offset.y + pan.y).coerceIn(-maxY, maxY)
                    )
                } else {
                    offset = Offset.Zero
                }
            }
        }
    } else {
        Modifier
    }

    AsyncImage(
        model = uri,
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offset.x
                translationY = offset.y
            }
            .then(tapModifier)
            .then(transformModifier)
    )
}

