package com.kdgm.lumagallery.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kdgm.lumagallery.feature.albums.AlbumsScreen
import com.kdgm.lumagallery.feature.gallery.GalleryScreen
import com.kdgm.lumagallery.feature.viewer.ViewerScreen
import com.kdgm.lumagallery.ui.navigation.AppDestination

@Composable
fun AppRoot() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestination.Gallery.route
    ) {
        composable(AppDestination.Gallery.route) {
            GalleryScreen()
        }
        composable(AppDestination.Albums.route) {
            AlbumsScreen()
        }
        composable(AppDestination.Viewer.route) {
            ViewerScreen()
        }
    }
}


