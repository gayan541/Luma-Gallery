package com.kdgm.lumagallery.feature.viewer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.kdgm.lumagallery.core.media.ImageMedia
import com.kdgm.lumagallery.ui.system.ImmersiveSystemUi

@Composable
fun ViewerScreen(
    images: List<ImageMedia>,
    startIndex: Int,
    onExit: () -> Unit
) {

    ImmersiveSystemUi(immersive = true)

    BackHandler {
        onExit()
    }

    val pagerState = rememberPagerState(
        initialPage = startIndex,
        pageCount = { images.size }
    )

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) { page ->

        AsyncImage(
            model = images[page].uri,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }


}
