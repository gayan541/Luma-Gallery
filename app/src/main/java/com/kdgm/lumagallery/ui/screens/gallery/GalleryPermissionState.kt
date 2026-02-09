package com.kdgm.lumagallery.ui.screens.gallery

sealed class GalleryPermissionState {
    object Checking : GalleryPermissionState()
    object Granted : GalleryPermissionState()
    object Denied : GalleryPermissionState()
    object PermanentlyDenied : GalleryPermissionState()
}
