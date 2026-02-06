package com.kdgm.lumagallery.feature.gallery

import com.kdgm.lumagallery.core.media.ImageMedia

data class GalleryUiState(
    val isLoading: Boolean = true,
    val images: List<ImageMedia> = emptyList(),
    val error: String? = null
)

