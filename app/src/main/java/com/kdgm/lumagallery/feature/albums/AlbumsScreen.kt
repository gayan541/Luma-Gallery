package com.kdgm.lumagallery.feature.albums

import androidx.compose.runtime.Composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kdgm.lumagallery.core.media.Album

@Composable
fun AlbumsScreen(
    viewModel: AlbumsViewModel,
    onAlbumOpen: (Album) -> Unit
) {

    val state by viewModel.state.collectAsState()

    when {
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        else -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    state.albums.size,
                    key = { state.albums[it].bucketId }
                ) { index ->

                    val album = state.albums[index]

                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                onAlbumOpen(album)
                            }
                    ) {
                        AsyncImage(
                            model = album.coverUri,
                            contentDescription = album.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(album.name)
                        Text("${album.count} items")
                    }
                }
            }
        }
    }


}
