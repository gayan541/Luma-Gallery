package com.kdgm.lumagallery.data.repository

import com.kdgm.lumagallery.data.datasource.MediaStoreAlbumDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AlbumRepositoryImpl(
    private val dataSource: MediaStoreAlbumDataSource
) : AlbumRepository {

    override fun getAlbums() = flow {
        emit(dataSource.getAlbums())
    }.flowOn(Dispatchers.IO)


}
