package com.kdgm.lumagallery.ui.system

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun ImmersiveSystemUi(
    immersive: Boolean
) {
    val context = LocalContext.current
    val activity = context as Activity

    DisposableEffect(immersive) {

        val window = activity.window
        val controller =
            WindowCompat.getInsetsController(window, window.decorView)

        if (immersive) {
            controller?.hide(
                WindowInsetsCompat.Type.systemBars()
            )
            controller?.systemBarsBehavior =
                WindowInsetsControllerCompat
                    .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            controller?.show(
                WindowInsetsCompat.Type.systemBars()
            )
        }

        onDispose {
            controller?.show(
                WindowInsetsCompat.Type.systemBars()
            )
        }
    }


}
