package com.kdgm.lumagallery.ui.screens.viewer

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

data class PhotoInfo(
    val filename: String,
    val path: String,
    val size: String,
    val resolution: String,
    val dateTaken: String,
    val dateModified: String,
    val mimeType: String
)

@Composable
fun PhotoInfoDialog(
    uri: Uri,
    context: Context,
    onDismiss: () -> Unit
) {
    var photoInfo by remember { mutableStateOf<PhotoInfo?>(null) }

    LaunchedEffect(uri) {
        photoInfo = getPhotoInfo(context, uri)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Photo Details") },
        text = {
            photoInfo?.let { info ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoRow("Filename", info.filename)
                    InfoRow("Path", info.path)
                    InfoRow("Size", info.size)
                    InfoRow("Resolution", info.resolution)
                    InfoRow("Date taken", info.dateTaken)
                    InfoRow("Modified", info.dateModified)
                    InfoRow("Type", info.mimeType)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

private fun getPhotoInfo(context: Context, uri: Uri): PhotoInfo? {
    val projection = arrayOf(
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media.WIDTH,
        MediaStore.Images.Media.HEIGHT,
        MediaStore.Images.Media.DATE_TAKEN,
        MediaStore.Images.Media.DATE_MODIFIED,
        MediaStore.Images.Media.MIME_TYPE
    )

    context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val pathIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
            val widthIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
            val heightIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)
            val dateTakenIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val dateModifiedIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)
            val mimeTypeIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)

            val filename = cursor.getString(nameIndex) ?: "Unknown"
            val path = cursor.getString(pathIndex) ?: "Unknown"
            val size = formatFileSize(cursor.getLong(sizeIndex))
            val width = cursor.getInt(widthIndex)
            val height = cursor.getInt(heightIndex)
            val resolution = "${width} × ${height}"
            val dateTaken = formatDate(cursor.getLong(dateTakenIndex))
            val dateModified = formatDate(cursor.getLong(dateModifiedIndex) * 1000)
            val mimeType = cursor.getString(mimeTypeIndex) ?: "Unknown"

            return PhotoInfo(
                filename = filename,
                path = path,
                size = size,
                resolution = resolution,
                dateTaken = dateTaken,
                dateModified = dateModified,
                mimeType = mimeType
            )
        }
    }
    return null
}

private fun formatFileSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> String.format("%.2f KB", bytes / 1024.0)
        bytes < 1024 * 1024 * 1024 -> String.format("%.2f MB", bytes / (1024.0 * 1024))
        else -> String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024))
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
