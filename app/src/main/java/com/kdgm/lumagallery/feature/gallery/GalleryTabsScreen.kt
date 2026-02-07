package com.kdgm.lumagallery.feature.gallery

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import com.kdgm.lumagallery.feature.albums.AlbumsScreen
import com.kdgm.lumagallery.feature.albums.AlbumsViewModel
import com.kdgm.lumagallery.data.datasource.MediaStoreAlbumDataSource
import com.kdgm.lumagallery.data.repository.AlbumRepositoryImpl
import androidx.compose.ui.platform.LocalContext

@Composable
fun GalleryTabsScreen(
    galleryViewModel: GalleryViewModel,
    onImageOpen: (Int) -> Unit,
    onAlbumOpen: (Long, String) -> Unit
) {

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Photos", "Albums")

    Column(modifier = Modifier.fillMaxSize()) {

        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> {
                GalleryScreen(
                    viewModel = galleryViewModel,
                    onImageOpen = onImageOpen
                )
            }

            1 -> {
                val context = LocalContext.current

                val albumsViewModel = remember {
                    AlbumsViewModel(
                        AlbumRepositoryImpl(
                            MediaStoreAlbumDataSource(context)
                        )
                    )
                }

                AlbumsScreen(
                    viewModel = albumsViewModel,
                    onAlbumOpen = { album ->
                        onAlbumOpen(album.bucketId, album.name)
                    }
                )
            }
        }
    }


}
