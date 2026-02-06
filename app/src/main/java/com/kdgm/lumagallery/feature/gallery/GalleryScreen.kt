package com.kdgm.lumagallery.feature.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.kdgm.lumagallery.data.datasource.MediaStoreImageDataSource
import com.kdgm.lumagallery.data.repository.ImageRepositoryImpl

@Composable
fun GalleryScreen() {
    val context = LocalContext.current

    val viewModel = remember {
        GalleryViewModel(
            imageRepository = ImageRepositoryImpl(
                MediaStoreImageDataSource(context)
            )
        )
    }

    val state by viewModel.uiState.collectAsState()

    // UI will be added next step
}

