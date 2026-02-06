package com.kdgm.lumagallery.data.repository

import com.kdgm.lumagallery.core.media.ImageMedia
import com.kdgm.lumagallery.data.datasource.MediaStoreImageDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ImageRepositoryImpl(
    private val dataSource: MediaStoreImageDataSource
) : ImageRepository {

    override fun getImages(): Flow<List<ImageMedia>> = flow {
        emit(dataSource.getImages())
    }.flowOn(Dispatchers.IO)
}

