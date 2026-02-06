package com.kdgm.lumagallery.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween

private const val DURATION = 280

val enterViewerAnimation =
    slideInVertically(
        initialOffsetY = { it / 12 },
        animationSpec = tween(DURATION)
    ) + fadeIn(animationSpec = tween(DURATION))

val exitViewerAnimation =
    slideOutVertically(
        targetOffsetY = { it / 12 },
        animationSpec = tween(DURATION)
    ) + fadeOut(animationSpec = tween(DURATION))

val popEnterViewerAnimation =
    slideInVertically(
        initialOffsetY = { it / 12 },
        animationSpec = tween(DURATION)
    ) + fadeIn(animationSpec = tween(DURATION))

val popExitViewerAnimation =
    slideOutVertically(
        targetOffsetY = { it / 12 },
        animationSpec = tween(DURATION)
    ) + fadeOut(animationSpec = tween(DURATION))
