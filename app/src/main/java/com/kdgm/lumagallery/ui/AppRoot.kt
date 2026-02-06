package com.kdgm.lumagallery.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kdgm.lumagallery.data.datasource.MediaStoreImageDataSource
import com.kdgm.lumagallery.data.repository.ImageRepositoryImpl
import com.kdgm.lumagallery.feature.gallery.GalleryScreen
import com.kdgm.lumagallery.feature.gallery.GalleryViewModel
import com.kdgm.lumagallery.feature.viewer.ViewerScreen
import com.kdgm.lumagallery.ui.navigation.AppDestination

@Composable
fun AppRoot() {

    val context = LocalContext.current

    val galleryViewModel = remember {
        GalleryViewModel(
            ImageRepositoryImpl(
                MediaStoreImageDataSource(context)
            )
        )
    }

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestination.Gallery.route
    ) {

        composable(AppDestination.Gallery.route) {
            GalleryScreen(
                viewModel = galleryViewModel,
                onImageOpen = { index ->
                    navController.navigate(
                        AppDestination.Viewer.createRoute(index)
                    )
                }
            )
        }

        composable(
            route = AppDestination.Viewer.route,
            arguments = listOf(
                navArgument("index") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val index =
                backStackEntry.arguments?.getInt("index") ?: 0

            ViewerScreen(
                images = galleryViewModel.getImages(),
                startIndex = index,
                onExit = {
                    navController.popBackStack()
                }
            )
        }
    }


}



