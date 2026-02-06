package com.kdgm.lumagallery.feature.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdgm.lumagallery.data.repository.ImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class GalleryViewModel(
    private val imageRepository: ImageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GalleryUiState())
    val uiState: StateFlow<GalleryUiState> = _uiState.asStateFlow()

    init {
        loadImages()
    }

    private fun loadImages() {
        viewModelScope.launch {
            imageRepository.getImages()
                .onStart {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
                .catch { throwable ->
                    _uiState.value = GalleryUiState(
                        isLoading = false,
                        error = throwable.message
                    )
                }
                .collect { images ->
                    _uiState.value = GalleryUiState(
                        isLoading = false,
                        images = images
                    )
                }
        }
    }
}

