package com.kdgm.lumagallery.ui.screens.gallery.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun GalleryBottomBar(
    navController: NavController,
    currentRoute: String
) {
    NavigationBar(
        containerColor = Color.Black,
        modifier = Modifier.navigationBarsPadding()
    ) {

        NavigationBarItem(
            selected = currentRoute == "gallery",
            onClick = {
                if (currentRoute != "gallery")
                    navController.navigate("gallery") {
                        popUpTo("gallery") { inclusive = true }
                    }
            },
            icon = { Icon(Icons.Default.Image, null) },
            label = { Text("Pictures") }
        )

        NavigationBarItem(
            selected = currentRoute == "albums",
            onClick = {
                if (currentRoute != "albums")
                    navController.navigate("albums")
            },
            icon = { Icon(Icons.Default.Collections, null) },
            label = { Text("Albums") }
        )

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Bookmark, null) },
            label = { Text("Stories") }
        )

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Menu, null) },
            label = { Text("Menu") }
        )
    }
}

