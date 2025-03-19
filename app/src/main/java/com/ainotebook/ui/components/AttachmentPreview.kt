package com.ainotebook.ui.components

import android.webkit.MimeTypeMap
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.io.File

@Composable
fun AttachmentPreview(
    attachments: List<String>,
    onRemoveAttachment: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(attachments) { path ->
            AttachmentItem(
                path = path,
                onRemove = { onRemoveAttachment(path) }
            )
        }
    }
}

@Composable
private fun AttachmentItem(
    path: String,
    onRemove: () -> Unit
) {
    val file = File(path)
    val mimeType = MimeTypeMap.getSingleton()
        .getMimeTypeFromExtension(file.extension)
        ?: "application/octet-stream"
    
    Card(
        modifier = Modifier.size(100.dp),
        elevation = 4.dp
    ) {
        Box {
            when {
                mimeType.startsWith("image/") -> {
                    AsyncImage(
                        model = file,
                        contentDescription = "Image attachment",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                mimeType.startsWith("audio/") -> {
                    Icon(
                        imageVector = Icons.Filled.AudioFile,
                        contentDescription = "Audio attachment",
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center)
                    )
                }
                else -> {
                    Icon(
                        imageVector = Icons.Filled.InsertDriveFile,
                        contentDescription = "File attachment",
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center)
                    )
                }
            }
            
            IconButton(
                onClick = onRemove,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Remove attachment",
                    tint = MaterialTheme.colors.error
                )
            }
        }
    }
} 