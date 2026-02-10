package com.kdgm.lumagallery.ui.screens.gallery.box

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Slideshow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ViewerTopBar(
    onBack: () -> Unit,
    onSlideshow: () -> Unit = {},
    currentIndex: Int = 0,
    totalCount: Int = 0
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = { /* TODO: Show info dialog */ }) {
                    Icon(Icons.Default.Info, contentDescription = "Info", tint = Color.White)
                }

                IconButton(onClick = onSlideshow) {
                    Icon(
                        Icons.Outlined.Slideshow,
                        contentDescription = "Slideshow",
                        tint = Color.White
                    )
                }

                IconButton(onClick = { /* TODO: More menu */ }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = Color.White
                    )
                }
            }
        }

        // Image counter
        if (totalCount > 0) {
            Text(
                text = "${currentIndex + 1} / $totalCount",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(start = 20.dp, bottom = 8.dp)
            )
        }
    }
}
