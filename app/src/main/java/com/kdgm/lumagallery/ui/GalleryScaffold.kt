package com.kdgm.lumagallery.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.kdgm.lumagallery.feature.albums.AlbumsScreen
import com.kdgm.lumagallery.feature.albums.AlbumsViewModel
import com.kdgm.lumagallery.feature.gallery.GalleryScreen
import com.kdgm.lumagallery.feature.gallery.GalleryViewModel
import com.kdgm.lumagallery.ui.navigation.BottomNavItem
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kdgm.lumagallery.data.datasource.MediaStoreAlbumDataSource
import com.kdgm.lumagallery.data.repository.AlbumRepositoryImpl
import com.kdgm.lumagallery.ui.sheet.MenuBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScaffold(
    galleryViewModel: GalleryViewModel,
    onImageOpen: (Int) -> Unit
) {

    var selectedTab by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Pictures) }
    var showMenu by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val albumsViewModel = remember {
        AlbumsViewModel(
            AlbumRepositoryImpl(
                MediaStoreAlbumDataSource(context)
            )
        )
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf(
                    BottomNavItem.Pictures,
                    BottomNavItem.Albums,
                    BottomNavItem.Stories,
                    BottomNavItem.Menu
                ).forEach { item ->

                    NavigationBarItem(
                        selected = selectedTab == item,
                        onClick = {
                            if (item is BottomNavItem.Menu) {
                                showMenu = true
                            } else {
                                selectedTab = item
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(item.label)
                        }
                    )
                }
            }
        }
    ) { padding ->

        when (selectedTab) {

            is BottomNavItem.Pictures -> {
                GalleryScreen(
                    viewModel = galleryViewModel,
                    onImageOpen = onImageOpen
                )
            }

            is BottomNavItem.Albums -> {
                AlbumsScreen(
                    viewModel = albumsViewModel,
                    onAlbumOpen = { /* wired in Step 4.2 */ }
                )
            }

            is BottomNavItem.Stories -> {
                Box(
                    modifier = Modifier.padding(padding)
                ) {
                    Text(
                        text = "Stories (Coming soon)",
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }

            else -> {}
        }

        if (showMenu) {
            MenuBottomSheet(
                onDismiss = { showMenu = false }
            )
        }
    }


}
