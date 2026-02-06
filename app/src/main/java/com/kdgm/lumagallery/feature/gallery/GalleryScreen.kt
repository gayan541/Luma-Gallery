package com.kdgm.lumagallery.feature.gallery

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import com.kdgm.lumagallery.data.datasource.MediaStoreImageDataSource
import com.kdgm.lumagallery.data.repository.ImageRepositoryImpl

@Composable
fun GalleryScreen() {
    val context = LocalContext.current

    val viewModel = remember {
        GalleryViewModel(
            ImageRepositoryImpl(
                MediaStoreImageDataSource(context)
            )
        )
    }

    var zoomState by remember {
        mutableStateOf(GridZoomState(columns = 3))
    }

    val state by viewModel.uiState.collectAsState()

    when {
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.images.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No photos found")
            }
        }

        else -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(zoomState.columns),
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures { _, _, zoom, _ ->
                            val newColumns = when {
                                zoom > 1.05f -> zoomState.columns - 1
                                zoom < 0.95f -> zoomState.columns + 1
                                else -> zoomState.columns
                            }

                            zoomState = zoomState.copy(
                                columns = newColumns.coerceIn(
                                    zoomState.minColumns,
                                    zoomState.maxColumns
                                )
                            )
                        }
                    }
            ) {
                items(
                    items = state.images,
                    key = { it.id }
                ) { image ->
                    GalleryGridItem(image)
                }
            }

        }
    }
}

