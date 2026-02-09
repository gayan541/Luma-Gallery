package com.kdgm.lumagallery.ui.screens.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.kdgm.lumagallery.ui.screens.gallery.components.*

@Composable
fun GalleryContent(
    viewModel: GalleryViewModel,
    navController: NavController,
    onImageClick: (Int) -> Unit
) {
    val images by viewModel.images.collectAsState()
    val groupedImages by viewModel.groupedImages.collectAsState()
    val selectedIds by viewModel.selectedIds.collectAsState()
    val isSelectionMode by viewModel.isSelectionMode.collectAsState()

    val allSelected = images.isNotEmpty() && selectedIds.size == images.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        if (isSelectionMode) {
            SelectionTopBar(
                count = selectedIds.size,
                allSelected = allSelected,
                onSelectAll = {
                    if (allSelected) viewModel.clearSelection()
                    else viewModel.selectAll(images)
                },
                onCancel = { viewModel.clearSelection() }
            )
        } else {
            GalleryTopBar(
                onSelectClick = {
                    if (images.isNotEmpty()) {
                        viewModel.selectAll(images)
                    }
                },
                onSlideshowClick = {
                    navController.navigate("slideshow")
                }
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            StickyGroupedPhotoGrid(
                groupedImages = groupedImages,
                allImages = images,
                selectedIds = selectedIds,
                onTap = { image ->
                    if (isSelectionMode) {
                        viewModel.toggleSelection(image)
                    } else {
                        onImageClick(images.indexOf(image))
                    }
                },
                onLongPress = { image ->
                    viewModel.toggleSelection(image)
                }
            )
        }

        if (isSelectionMode) {
            SelectionBottomBar(viewModel = viewModel)
        }
    }
}


