package com.kt.uptodo.presentation.components.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kt.uptodo.data.enums.Priority

@Composable
fun PriorityDialog(
    modifier: Modifier = Modifier,
    selectedPriority: Priority,
    onValueSelected: (Priority) -> Unit,
    onDismiss: () -> Unit
) {
    val priorities = Priority.toList()
    ListDialog(
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        items(
            items = priorities,
            key = { it.hashCode() }
        ) {
            TextButton(
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.textButtonColors(
                    containerColor = if (selectedPriority == it) MaterialTheme.colorScheme.primaryContainer else Color.Unspecified,
                    contentColor = if (selectedPriority == it) MaterialTheme.colorScheme.onPrimaryContainer else Color.Unspecified
                ),
                onClick = { onValueSelected(it) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = it.name,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}