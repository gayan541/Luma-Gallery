package com.kdgm.lumagallery.core.media

import android.net.Uri

data class ImageMedia(
    val id: Long,
    val uri: Uri,
    val name: String,
    val dateTaken: Long,
    val size: Long
)

