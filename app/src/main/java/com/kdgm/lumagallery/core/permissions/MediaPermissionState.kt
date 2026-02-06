package com.kdgm.lumagallery.core.permissions

sealed interface MediaPermissionState {
    data object Granted : MediaPermissionState
    data object Denied : MediaPermissionState
    data object PermanentlyDenied : MediaPermissionState
}
