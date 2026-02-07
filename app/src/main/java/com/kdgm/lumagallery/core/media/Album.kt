package com.kdgm.lumagallery.core.media

data class Album(
    val bucketId: Long,
    val name: String,
    val coverUri: android.net.Uri,
    val count: Int
)
