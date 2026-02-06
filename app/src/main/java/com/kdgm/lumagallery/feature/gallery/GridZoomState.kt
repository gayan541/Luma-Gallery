package com.kdgm.lumagallery.feature.gallery

data class GridZoomState(
    val columns: Int = 3,
    val minColumns: Int = 2,
    val maxColumns: Int = 15
)

