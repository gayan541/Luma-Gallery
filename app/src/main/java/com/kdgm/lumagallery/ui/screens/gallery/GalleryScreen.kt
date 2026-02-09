package com.kdgm.lumagallery.ui.screens.gallery

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.kdgm.lumagallery.ui.screens.gallery.components.GalleryBottomBar
import com.kdgm.lumagallery.ui.screens.gallery.components.PermissionRequestUI

@Composable
fun GalleryScreen(
    navController: NavController,
    viewModel: GalleryViewModel
) {
    val context = LocalContext.current
    val permissionState by viewModel.permissionState.collectAsState()
    val isSelectionMode by viewModel.isSelectionMode.collectAsState()

    val permission = if (Build.VERSION.SDK_INT >= 33)
        Manifest.permission.READ_MEDIA_IMAGES
    else
        Manifest.permission.READ_EXTERNAL_STORAGE

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.checkPermission(context)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.checkPermission(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        Box(modifier = Modifier.weight(1f)) {
            when (permissionState) {

                GalleryPermissionState.Checking -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                    )
                }

                GalleryPermissionState.Denied -> {
                    PermissionRequestUI(
                        onGrantClick = {
                            launcher.launch(permission)
                        }
                    )
                }

                GalleryPermissionState.Granted -> {
                    GalleryContent(
                        viewModel = viewModel,
                        navController = navController,
                        onImageClick = { index ->
                            navController.navigate("viewer/$index")
                        }
                    )
                }

                else -> {}
            }
        }

        // 🔥 IMPORTANT PART
        // Normal bottom bar ONLY when NOT selecting
        if (!isSelectionMode) {
            GalleryBottomBar(
                navController = navController,
                currentRoute = "gallery"
            )
        }
    }
}


