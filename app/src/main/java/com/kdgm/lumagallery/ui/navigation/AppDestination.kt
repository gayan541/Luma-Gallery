package com.kdgm.lumagallery.ui.navigation

sealed class AppDestination(val route: String) {
    data object Gallery : AppDestination("gallery")
    data object Albums : AppDestination("albums")
    data object Viewer : AppDestination("viewer")
}
