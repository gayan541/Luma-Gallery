package com.kdgm.lumagallery.ui.screens.gallery.model

import android.net.Uri

data class GalleryImage(
    val id: Long,
    val uri: Uri,
    val dateTaken: Long
)
