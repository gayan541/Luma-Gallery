package com.kdgm.lumagallery.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kdgm.lumagallery.core.permissions.MediaPermissionManager
import com.kdgm.lumagallery.data.datasource.MediaStoreImageDataSource
import com.kdgm.lumagallery.data.repository.ImageRepositoryImpl
import com.kdgm.lumagallery.feature.gallery.GalleryScreen
import com.kdgm.lumagallery.feature.gallery.GalleryViewModel
import com.kdgm.lumagallery.feature.permission.PermissionScreen
import com.kdgm.lumagallery.feature.viewer.ViewerScreen
import com.kdgm.lumagallery.ui.navigation.AppDestination
import com.kdgm.lumagallery.ui.navigation.AppStartDestination

@Composable
fun AppRoot() {

    val context = LocalContext.current
    val navController = rememberNavController()

    val permissionManager = remember {
        MediaPermissionManager(context)
    }

    val startDestination by remember {
        mutableStateOf(
            if (permissionManager.isPermissionGranted()) {
                AppStartDestination.GALLERY
            } else {
                AppStartDestination.PERMISSION
            }
        )
    }

    val galleryViewModel = remember {
        GalleryViewModel(
            ImageRepositoryImpl(
                MediaStoreImageDataSource(context)
            )
        )
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            if (permissionManager.isPermissionGranted()) {
                galleryViewModel.loadImages()

                navController.navigate(AppDestination.Gallery.route) {
                    popUpTo(0)
                }
            }
        }

    NavHost(
        navController = navController,
        startDestination =
            if (startDestination == AppStartDestination.GALLERY)
                AppDestination.Gallery.route
            else
                AppDestination.Permission.route
    ) {

        composable(AppDestination.Permission.route) {
            PermissionScreen(
                onRequestPermission = {
                    permissionLauncher.launch(
                        permissionManager.requiredPermissions()
                    )
                }
            )
        }

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


