package com.kdgm.lumagallery.ui.screens.gallery.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SelectionOverlay(
    selected: Boolean,
    isSelectionMode: Boolean
) {
    if (!isSelectionMode) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(22.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(22.dp)
                    .border(2.dp, Color.White, CircleShape)
            )
        }
    }
}
