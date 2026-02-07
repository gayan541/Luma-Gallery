package com.kdgm.lumagallery.feature.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdgm.lumagallery.core.media.Album
import com.kdgm.lumagallery.data.repository.AlbumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AlbumsUiState(
    val isLoading: Boolean = true,
    val albums: List<Album> = emptyList()
)

class AlbumsViewModel(
    private val repository: AlbumRepository
) : ViewModel() {

    private val _state =
        MutableStateFlow(AlbumsUiState())

    val state: StateFlow<AlbumsUiState> =
        _state.asStateFlow()

    init {
        loadAlbums()
    }

    private fun loadAlbums() {
        viewModelScope.launch {
            repository.getAlbums().collect {
                _state.value =
                    AlbumsUiState(
                        isLoading = false,
                        albums = it
                    )
            }
        }
    }


}
