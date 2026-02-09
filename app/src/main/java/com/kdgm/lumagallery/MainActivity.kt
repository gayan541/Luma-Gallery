package com.kdgm.lumagallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kdgm.lumagallery.ui.navigation.AppNavGraph
import com.kdgm.lumagallery.ui.theme.LumaGalleryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LumaGalleryTheme {
                AppNavGraph()
            }
        }
    }
}
