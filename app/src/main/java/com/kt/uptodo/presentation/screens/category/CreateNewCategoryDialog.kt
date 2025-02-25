package com.kt.uptodo.presentation.screens.category

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kt.uptodo.R
import com.kt.uptodo.extensions.toHex
import com.kt.uptodo.presentation.LocalDatabase
import com.kt.uptodo.presentation.components.dialog.DefaultDialog
import com.kt.uptodo.utils.DefaultCategory
import com.kt.uptodo.utils.DefaultCategoryColors
import com.kt.uptodo.utils.padding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@Composable
fun CreateNewCategoryDialog(
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val database = LocalDatabase.current
    var isComplete by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val lazyRowState = rememberLazyListState()
    val (category, onCategoryChange) = remember { mutableStateOf(DefaultCategory) }

    DefaultDialog(
        onDismiss = {
            if (isComplete) onDismiss()
        },
        title = {
            Text(
                text = stringResource(R.string.action_create_new_category)
            )
        },
        buttons = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = stringResource(R.string.action_cancel))
            }

            TextButton(
                onClick = {
                    scope.launch {
                        isLoading = true
                        withContext(Dispatchers.IO) {
                            database.insert(category)
                        }
                        isLoading = false
                        isComplete = true
                        onDismiss()
                    }
                },
                enabled = category.name.isNotEmpty() && !isLoading
            ) {
                Text(text = "Create category")
            }
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small)
        ) {
            OutlinedTextField(
                value = category.name,
                onValueChange = {
                    onCategoryChange(category.copy(name = it))
                },
                shape = MaterialTheme.shapes.small,
                placeholder = {
                    Text(
                        text = "Eg: Work",
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                supportingText = {
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            var selectedColor by remember { mutableStateOf(DefaultCategoryColors[0]) }
            LazyRow(
                state = lazyRowState,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small)
            ) {
                items(
                    items = DefaultCategoryColors,
                    key = { it.hashCode() }
                ) { color ->
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(color)
                            .clickable {
                                onCategoryChange(category.copy(color = color.toHex()))
                                selectedColor = color
                            }
                    ) {
                        if (selectedColor == color) {
                            Icon(
                                painter = painterResource(R.drawable.ic_check),
                                contentDescription = null,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}