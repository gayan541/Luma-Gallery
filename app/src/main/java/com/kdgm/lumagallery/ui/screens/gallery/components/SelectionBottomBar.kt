package com.kdgm.lumagallery.ui.screens.gallery.components

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.kdgm.lumagallery.ui.screens.gallery.GalleryViewModel

@Composable
fun SelectionBottomBar(
    viewModel: GalleryViewModel? = null
) {
    val context = LocalContext.current

    // Permission launcher for delete on Android 10+
    val deletePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            viewModel?.onDeletePermissionGranted()
            Toast.makeText(context, "Images deleted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Delete cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    NavigationBar(
        containerColor = Color.Black,
        modifier = Modifier.navigationBarsPadding()
    ) {
        NavigationBarItem(
            selected = false,
            onClick = {
                Toast.makeText(context, "Create album (coming soon)", Toast.LENGTH_SHORT).show()
            },
            icon = { Icon(Icons.Default.Add, null) },
            label = { Text("Create") }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                viewModel?.let { vm ->
                    shareSelectedImages(context, vm)
                }
            },
            icon = { Icon(Icons.Default.Share, null) },
            label = { Text("Share") }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                viewModel?.let { vm ->
                    deleteSelectedImages(context, vm, deletePermissionLauncher)
                }
            },
            icon = { Icon(Icons.Default.Delete, null) },
            label = { Text("Delete") }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                Toast.makeText(context, "More options", Toast.LENGTH_SHORT).show()
            },
            icon = { Icon(Icons.Default.MoreVert, null) },
            label = { Text("More") }
        )
    }
}

private fun shareSelectedImages(context: Context, viewModel: GalleryViewModel) {
    val selectedImages = viewModel.getSelectedImages()

    if (selectedImages.isEmpty()) {
        Toast.makeText(context, "No images selected", Toast.LENGTH_SHORT).show()
        return
    }

    val shareIntent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
        type = "image/*"
        putParcelableArrayListExtra(
            Intent.EXTRA_STREAM,
            ArrayList(selectedImages.map { it.uri })
        )
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(shareIntent, "Share images via"))
}

private fun deleteSelectedImages(
    context: Context,
    viewModel: GalleryViewModel,
    permissionLauncher: androidx.activity.result.ActivityResultLauncher<IntentSenderRequest>
) {
    viewModel.deleteSelectedImages(
        context = context,
        onNeedPermission = { intentSender ->
            // Launch permission request
            val request = IntentSenderRequest.Builder(intentSender).build()
            permissionLauncher.launch(request)
        },
        onComplete = { success ->
            if (success) {
                Toast.makeText(context, "Images deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to delete images", Toast.LENGTH_SHORT).show()
            }
        }
    )
}

