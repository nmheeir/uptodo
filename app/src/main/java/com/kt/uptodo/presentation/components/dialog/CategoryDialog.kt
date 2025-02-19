package com.kt.uptodo.presentation.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kt.uptodo.R
import com.kt.uptodo.data.entities.CategoryEntity
import com.kt.uptodo.utils.padding

@Composable
fun CategoryDialog(
    modifier: Modifier = Modifier,
    categories: List<CategoryEntity>,
    selectedCategory: CategoryEntity,
    onValueSelected: (CategoryEntity) -> Unit,
    onDismiss: () -> Unit,
    onCreateNewCategory: () -> Unit
) {
    ListDialog(
        modifier = modifier,
        onDismiss = onDismiss
    ) {
        items(
            items = categories,
            key = { it.hashCode() }
        ) { category ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.medium),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onValueSelected(category)
                        onDismiss()
                    }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(Color(android.graphics.Color.parseColor(category.color)))
                )
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = MaterialTheme.padding.medium)
                )
            }
        }
        item {
            TextButton(
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.textButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                onClick = onCreateNewCategory,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.padding.mediumSmall)
            ) {
                Text(
                    text = stringResource(R.string.action_create_new_category),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

}