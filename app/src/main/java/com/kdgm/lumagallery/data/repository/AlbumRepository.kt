package com.kdgm.lumagallery.data.repository

import com.kdgm.lumagallery.core.media.Album
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    fun getAlbums(): Flow<List<Album>>
}
