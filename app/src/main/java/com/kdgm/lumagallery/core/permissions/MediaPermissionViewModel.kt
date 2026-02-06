package com.kdgm.lumagallery.core.permissions

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MediaPermissionViewModel(
    private val permissionManager: MediaPermissionManager
) : ViewModel() {

    private val _state = MutableStateFlow<MediaPermissionState>(
        MediaPermissionState.Denied
    )
    val state: StateFlow<MediaPermissionState> = _state

    fun checkPermission() {
        _state.value = if (permissionManager.isPermissionGranted()) {
            MediaPermissionState.Granted
        } else {
            MediaPermissionState.Denied
        }
    }
}
