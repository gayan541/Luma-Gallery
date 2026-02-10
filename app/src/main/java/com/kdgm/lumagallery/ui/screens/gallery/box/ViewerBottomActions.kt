package com.kdgm.lumagallery.ui.screens.gallery.box

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ActionButton(
            icon = { Icon(Icons.Outlined.FavoriteBorder, null, tint = Color.White) },
            label = "Favorite",
            onClick = {
                Toast.makeText(context, "Add to favorites", Toast.LENGTH_SHORT).show()
            }
        )

        ActionButton(
            icon = { Icon(Icons.Outlined.Edit, null, tint = Color.White) },
            label = "Edit",
            onClick = {
                Toast.makeText(context, "Edit (coming soon)", Toast.LENGTH_SHORT).show()
            }
        )

        ActionButton(
            icon = { Icon(Icons.Outlined.Share, null, tint = Color.White) },
            label = "Share",
            onClick = {
                currentImage?.let { shareImage(context, it) }
            }
        )

        ActionButton(
            icon = { Icon(Icons.Outlined.Delete, null, tint = Color.White) },
            label = "Delete",
            onClick = {
                Toast.makeText(context, "Delete (coming soon)", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
private fun ActionButton(
    icon: @Composable () -> Unit,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(70.dp)
    ) {
        IconButton(onClick = onClick) {
            icon()
        }
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
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
