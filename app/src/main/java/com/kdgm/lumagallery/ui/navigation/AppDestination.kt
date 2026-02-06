package com.kdgm.lumagallery.ui.navigation

sealed class AppDestination(val route: String) {

    data object Entry : AppDestination("entry")
    data object Gallery : AppDestination("gallery")

    data object Viewer : AppDestination("viewer/{index}") {
        fun createRoute(index: Int): String = "viewer/$index"
    }


}
