package com.kdgm.lumagallery.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val label: String,
    val icon: ImageVector
) {
    data object Pictures : BottomNavItem(
        label = "Pictures",
        icon = Icons.Filled.Collections
    )

    data object Albums : BottomNavItem(
        label = "Albums",
        icon = Icons.Filled.Folder
    )

    data object Stories : BottomNavItem(
        label = "Stories",
        icon = Icons.Filled.AutoStories
    )

    data object Menu : BottomNavItem(
        label = "Menu",
        icon = Icons.Filled.Menu
    )


}
