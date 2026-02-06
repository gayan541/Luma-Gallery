package com.kdgm.lumagallery.feature.gallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import com.kdgm.lumagallery.data.datasource.MediaStoreImageDataSource
import com.kdgm.lumagallery.data.repository.ImageRepositoryImpl
import kotlinx.coroutines.launch

@Composable
fun GalleryScreen(
    onImageOpen: (Int) -> Unit
) {

    val context = LocalContext.current
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    val viewModel = remember {
        GalleryViewModel(
            ImageRepositoryImpl(
                MediaStoreImageDataSource(context)
            )
        )
    }

    val state by viewModel.uiState.collectAsState()

    var zoomState by remember {
        mutableStateOf(GridZoomState())
    }

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
                state = gridState,
                columns = GridCells.Fixed(zoomState.columns),
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures { centroid, _, zoom, _ ->

                            val accumulatedZoom =
                                zoomState.zoomAccumulator * zoom

                            var newColumns = zoomState.columns

                            if (accumulatedZoom > 1.2f) newColumns--
                            else if (accumulatedZoom < 0.8f) newColumns++

                            newColumns = newColumns.coerceIn(
                                zoomState.minColumns,
                                zoomState.maxColumns
                            )

                            if (newColumns != zoomState.columns) {

                                val focusedIndex =
                                    calculateFocusedIndex(
                                        gridState,
                                        state.images,
                                        centroid
                                    )

                                zoomState = zoomState.copy(
                                    columns = newColumns,
                                    zoomAccumulator = 1f
                                )

                                if (newColumns <= 2) {
                                    onImageOpen(focusedIndex)
                                } else {
                                    coroutineScope.launch {
                                        gridState.scrollToItem(focusedIndex)
                                    }
                                }

                            } else {
                                zoomState = zoomState.copy(
                                    zoomAccumulator = accumulatedZoom
                                )
                            }
                        }
                    }
            ) {
                items(
                    items = state.images,
                    key = { it.id }
                ) { image ->
                    GalleryGridItem(
                        image = image,
                        modifier = Modifier.clickable {
                            onImageOpen(
                                state.images.indexOf(image)
                            )
                        }
                    )
                }
            }
        }
    }


}

