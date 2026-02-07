package com.kdgm.lumagallery.ui.sheet

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuBottomSheet(
    onDismiss: () -> Unit
) {

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {

            MenuItem("Videos", Icons.Default.PlayArrow)
            MenuItem("Favorites", Icons.Default.Favorite)
            MenuItem("Recent", Icons.Default.History)
            MenuItem("Locations", Icons.Default.Place)

            Spacer(modifier = Modifier.height(16.dp))

            MenuItem("Shared albums", Icons.Default.People)
            MenuItem("Clean out", Icons.Default.CleaningServices)

            Spacer(modifier = Modifier.height(16.dp))

            MenuItem("Trash", Icons.Default.Delete)
            MenuItem("Settings", Icons.Default.Settings)
        }
    }


}

@Composable
private fun MenuItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(icon, contentDescription = title)
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, style = MaterialTheme.typography.bodyLarge)
    }
}
