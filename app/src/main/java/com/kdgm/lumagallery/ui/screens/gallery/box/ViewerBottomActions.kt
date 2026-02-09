package com.kdgm.lumagallery.ui.screens.gallery.box

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kdgm.lumagallery.ui.screens.gallery.model.GalleryImage

@Composable
fun ViewerBottomActions(
    currentImage: GalleryImage? = null
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = {
            Toast.makeText(context, "Add to favorites", Toast.LENGTH_SHORT).show()
        }) {
            Icon(Icons.Default.FavoriteBorder, null, tint = Color.White)
        }

        IconButton(onClick = {
            Toast.makeText(context, "Edit (coming soon)", Toast.LENGTH_SHORT).show()
        }) {
            Icon(Icons.Default.Edit, null, tint = Color.White)
        }

        IconButton(onClick = {
            currentImage?.let { shareImage(context, it) }
        }) {
            Icon(Icons.Default.Share, null, tint = Color.White)
        }

        IconButton(onClick = {
            Toast.makeText(context, "Delete (coming soon)", Toast.LENGTH_SHORT).show()
        }) {
            Icon(Icons.Default.Delete, null, tint = Color.White)
        }
    }
}

private fun shareImage(context: Context, image: GalleryImage) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM, image.uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share image via"))
}
