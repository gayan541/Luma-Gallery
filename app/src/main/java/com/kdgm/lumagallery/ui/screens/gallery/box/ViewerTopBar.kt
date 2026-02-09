package com.kdgm.lumagallery.ui.screens.gallery.box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ViewerTopBar(
    onBack: () -> Unit,
    onSlideshow: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, null, tint = Color.White)
        }

        Row {
            IconButton(onClick = { /* TODO: Show info dialog */ }) {
                Icon(Icons.Default.Info, null, tint = Color.White)
            }

            IconButton(onClick = onSlideshow) {
                Icon(Icons.Default.PlayArrow, null, tint = Color.White)
            }

            IconButton(onClick = { /* TODO: More menu */ }) {
                Icon(Icons.Default.MoreVert, null, tint = Color.White)
            }
        }
    }
}
