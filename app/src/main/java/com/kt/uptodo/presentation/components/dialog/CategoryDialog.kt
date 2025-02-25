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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kt.uptodo.R
import com.kt.uptodo.data.entities.CategoryEntity
import com.kt.uptodo.extensions.parseColor
import com.kt.uptodo.presentation.LocalDatabase
import com.kt.uptodo.presentation.screens.category.CreateNewCategoryDialog
import com.kt.uptodo.utils.padding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun CategoryDialog(
    modifier: Modifier = Modifier,
    selectedCategory: CategoryEntity? = null,
    onValueSelected: (CategoryEntity) -> Unit,
    onDismiss: () -> Unit,
) {
    val database = LocalDatabase.current
    var categories: List<CategoryEntity>? by remember {
        mutableStateOf(null)
    }
    var showCreateNewCategory by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            categories = database.categories()
        }
    }

    ListDialog(
        modifier = modifier,
        onDismiss = onDismiss
    ) {
        if (categories.isNullOrEmpty()) {
            item {
                Text(
                    text = "akdf",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        categories.takeIf { !it.isNullOrEmpty() }?.let { c ->
            items(
                items = c,
                key = { it.hashCode() }
            ) { category ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.medium),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (selectedCategory == category) MaterialTheme.colorScheme.inverseOnSurface
                            else Color.Transparent
                        )
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
                            .background(Color.parseColor(category.color))
                    )
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = MaterialTheme.padding.medium)
                    )
                }
            }
        }
        item {
            TextButton(
                onClick = {
                    showCreateNewCategory = true
                },
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

    if (showCreateNewCategory) {
        Dialog(
            onDismissRequest = { showCreateNewCategory = false }
        ) {
            CreateNewCategoryDialog(
                onDismiss = { showCreateNewCategory = false }
            )
        }
    }
}