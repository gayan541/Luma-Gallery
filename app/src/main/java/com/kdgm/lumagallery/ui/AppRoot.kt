package com.kdgm.lumagallery.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kdgm.lumagallery.core.permissions.MediaPermissionManager
import com.kdgm.lumagallery.core.permissions.MediaPermissionViewModel
import com.kdgm.lumagallery.feature.entry.EntryScreen
import com.kdgm.lumagallery.feature.gallery.GalleryScreen
import com.kdgm.lumagallery.feature.permission.PermissionScreen
import com.kdgm.lumagallery.ui.navigation.AppDestination

@Composable
fun AppRoot() {
    val context = LocalContext.current
    val navController = rememberNavController()

    val permissionManager = remember {
        MediaPermissionManager(context)
    }

    val viewModel = remember {
        MediaPermissionViewModel(permissionManager)
    }

    val permissionState by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkPermission()
    }

    NavHost(
        navController = navController,
        startDestination = AppDestination.Entry.route
    ) {
        composable(AppDestination.Entry.route) {
            EntryScreen(
                permissionState = permissionState,
                onPermissionGranted = {
                    navController.navigate(AppDestination.Gallery.route) {
                        popUpTo(AppDestination.Entry.route) { inclusive = true }
                    }
                },
                onPermissionRequired = {
                    navController.navigate("permission") {
                        popUpTo(AppDestination.Entry.route) { inclusive = true }
                    }
                }
            )
        }

        composable("permission") {
            PermissionScreen(
                onRequestPermission = {
                    // Permission launcher comes in next step
                }
            )
        }

        composable(AppDestination.Gallery.route) {
            GalleryScreen()
        }
    }
}



