package com.kdgm.lumagallery.ui.screens.gallery.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Slideshow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GalleryTopBar(
    onSelectClick: () -> Unit,
    onSlideshowClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .statusBarsPadding()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "More",
                    tint = Color.White
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Select items") },
                    onClick = {
                        expanded = false
                        onSelectClick()
                    },
                    leadingIcon = {
                        Icon(Icons.Outlined.CheckCircle, contentDescription = null)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Start slideshow") },
                    onClick = {
                        expanded = false
                        onSlideshowClick()
                    },
                    leadingIcon = {
                        Icon(Icons.Outlined.Slideshow, contentDescription = null)
                    }
                )
            }
        }
    }
}
