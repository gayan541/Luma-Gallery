package com.kdgm.lumagallery.feature.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdgm.lumagallery.data.repository.ImageRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class GalleryViewModel(
    private val imageRepository: ImageRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(GalleryUiState())

    val uiState: StateFlow<GalleryUiState> =
        _uiState.asStateFlow()

    private var hasRetriedAfterPermission = false

    init {
        loadImages()
    }

    fun loadImages() {
        viewModelScope.launch {
            imageRepository.getImages()
                .onStart {
                    _uiState.value =
                        _uiState.value.copy(isLoading = true)
                }
                .catch { error ->
                    _uiState.value =
                        GalleryUiState(
                            isLoading = false,
                            error = error.message
                        )
                }
                .collect { images ->

                    // 🔥 KEY FIX: handle first permission race condition
                    if (images.isEmpty() && !hasRetriedAfterPermission) {
                        hasRetriedAfterPermission = true

                        delay(400) // allow MediaStore to stabilize
                        loadImages()
                        return@collect
                    }

                    _uiState.value =
                        GalleryUiState(
                            isLoading = false,
                            images = images
                        )
                }
        }
    }

    fun getImages() = _uiState.value.images


}

