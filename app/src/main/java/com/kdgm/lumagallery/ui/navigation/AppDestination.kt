package com.kdgm.lumagallery.ui.navigation

sealed class AppDestination(val route: String) {

    data object Permission : AppDestination("permission")

    data object GalleryTabs : AppDestination("gallery_tabs")

    data object AlbumImages : AppDestination("album/{bucketId}/{name}") {
        fun createRoute(bucketId: Long, name: String): String =
            "album/$bucketId/$name"
    }

    data object Viewer : AppDestination("viewer/{index}") {
        fun createRoute(index: Int): String = "viewer/$index"
    }


}
