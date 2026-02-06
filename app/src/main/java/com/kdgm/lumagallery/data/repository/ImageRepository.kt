package com.kdgm.lumagallery.data.repository

import com.kdgm.lumagallery.core.media.ImageMedia
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    fun getImages(): Flow<List<ImageMedia>>
}

