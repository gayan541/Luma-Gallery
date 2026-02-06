package com.kdgm.lumagallery.feature.entry

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.kdgm.lumagallery.core.permissions.MediaPermissionState

@Composable
fun EntryScreen(
    permissionState: MediaPermissionState,
    onPermissionGranted: () -> Unit,
    onPermissionRequired: () -> Unit
) {
    LaunchedEffect(permissionState) {
        when (permissionState) {
            MediaPermissionState.Granted -> onPermissionGranted()
            else -> onPermissionRequired()
        }
    }
}
