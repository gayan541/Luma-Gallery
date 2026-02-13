package com.kdgm.lumagallery.ui.screens.gallery.box

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Slideshow
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    onInfo: () -> Unit = {},
    onRotateLeft: () -> Unit = {},
    onRotateRight: () -> Unit = {},
    onSetAsWallpaper: () -> Unit = {},
    onPrint: () -> Unit = {},
    onMenuOpenChange: (Boolean) -> Unit = {},
    currentIndex: Int = 0,
    totalCount: Int = 0
) {
    var showMoreMenu by remember { mutableStateOf(false) }

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
                IconButton(onClick = onInfo) {
                    Icon(Icons.Default.Info, contentDescription = "Info", tint = Color.White)
                }

                IconButton(onClick = onSlideshow) {
                    Icon(
                        Icons.Outlined.Slideshow,
                        contentDescription = "Slideshow",
                        tint = Color.White
                    )
                }

                // More button with dropdown menu
                Box {
                    IconButton(
                        onClick = {
                            showMoreMenu = true
                            onMenuOpenChange(true)  // Report immediately
                        }
                    ) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = Color.White
                        )
                    }

                    DropdownMenu(
                        expanded = showMoreMenu,
                        onDismissRequest = {
                            showMoreMenu = false
                            onMenuOpenChange(false)  // Report immediately
                        }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Rotate left") },
                            onClick = {
                                onRotateLeft()
                                showMoreMenu = false
                                onMenuOpenChange(false)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Rotate right") },
                            onClick = {
                                onRotateRight()
                                showMoreMenu = false
                                onMenuOpenChange(false)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Set as wallpaper") },
                            onClick = {
                                onSetAsWallpaper()
                                showMoreMenu = false
                                onMenuOpenChange(false)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Print") },
                            onClick = {
                                onPrint()
                                showMoreMenu = false
                                onMenuOpenChange(false)
                            }
                        )
                    }
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
