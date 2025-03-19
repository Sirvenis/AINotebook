package com.ainotebook.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TagChip(
    tag: String,
    selected: Boolean = false,
    onSelect: (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier.padding(4.dp),
        elevation = 4.dp,
        shape = MaterialTheme.shapes.small,
        color = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.surface
    ) {
        Chip(
            text = tag,
            selected = selected,
            onClick = onSelect,
            onRemove = onRemove,
            textColor = if (selected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
        )
    }
}

@Composable
private fun Chip(
    text: String,
    selected: Boolean,
    onClick: (() -> Unit)?,
    onRemove: (() -> Unit)?,
    textColor: Color
) {
    val clickModifier = if (onClick != null) {
        Modifier.padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = if (onRemove != null) 0.dp else 12.dp)
    } else {
        Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
    }
    
    if (onClick != null) {
        TextButton(
            onClick = onClick,
            modifier = clickModifier
        ) {
            ChipContent(text, selected, onRemove, textColor)
        }
    } else {
        ChipContent(text, selected, onRemove, textColor)
    }
}

@Composable
private fun ChipContent(
    text: String,
    selected: Boolean,
    onRemove: (() -> Unit)?,
    textColor: Color
) {
    Text(
        text = text,
        color = textColor,
        style = MaterialTheme.typography.body2
    )
    if (onRemove != null) {
        IconButton(
            onClick = onRemove,
            modifier = Modifier.padding(end = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Remove tag",
                tint = textColor
            )
        }
    }
} 