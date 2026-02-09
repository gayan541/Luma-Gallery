package com.kdgm.lumagallery.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kdgm.lumagallery.ui.screens.albums.AlbumPhotosScreen
import com.kdgm.lumagallery.ui.screens.albums.AlbumsScreen
import com.kdgm.lumagallery.ui.screens.gallery.GalleryScreen
import com.kdgm.lumagallery.ui.screens.gallery.GalleryViewModel
import com.kdgm.lumagallery.ui.screens.slideshow.SlideshowScreen
import com.kdgm.lumagallery.ui.screens.viewer.PhotoViewerScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    val galleryViewModel: GalleryViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "gallery"
    ) {

        composable("gallery") {
            GalleryScreen(
                navController = navController,
                viewModel = galleryViewModel
            )
        }

        composable("albums") {
            AlbumsScreen(
                navController = navController
            )
        }

        composable(
            route = "viewer/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("index") ?: 0

            PhotoViewerScreen(
                startIndex = index,
                viewModel = galleryViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("slideshow") {
            SlideshowScreen(
                viewModel = galleryViewModel,
                onExit = { navController.popBackStack() }
            )
        }
    }
}
