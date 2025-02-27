package com.kt.uptodo.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kt.uptodo.R
import com.kt.uptodo.data.entities.CategoryEntity
import com.kt.uptodo.data.enums.Priority
import com.kt.uptodo.data.relations.TaskDetail
import com.kt.uptodo.extensions.convertToDeadline
import com.kt.uptodo.presentation.LocalDatabase
import com.kt.uptodo.presentation.components.dialog.DefaultDialog
import com.kt.uptodo.utils.Gap
import com.kt.uptodo.utils.padding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    taskDetail: TaskDetail,
    onClick: (() -> Unit)? = null
) {
    val database = LocalDatabase.current
    val scope = rememberCoroutineScope()

    var showDeleteTaskDialog by remember { mutableStateOf(false) }
    val dismissState = rememberSwipeToDismissBoxState(
        positionalThreshold = { totalDistance ->
            totalDistance * 0.9f
        },
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                Timber.d(dismissValue.toString())
                showDeleteTaskDialog = true
            }
            true
        }
    )

    SwipeToDismissBox(
        modifier = modifier,
        state = dismissState,
        backgroundContent = {
            SwipeBackground(
                dismissState = dismissState,
                endIcon = R.drawable.ic_delete
            )
        },
        enableDismissFromStartToEnd = false
    ) {
        Box(
            modifier = if (onClick != null) {
                Modifier
                    .clip(MaterialTheme.shapes.small)
                    .clickable { onClick() }
            } else Modifier.clip(MaterialTheme.shapes.small)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .fillMaxWidth()
                    .padding(MaterialTheme.padding.small)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = taskDetail.task.title,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = taskDetail.task.deadline.convertToDeadline(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.weight(1f)
                        ) {
                            CategoryItem(category = taskDetail.category)
                            Gap(width = MaterialTheme.padding.small)
                            PriorityItem(priority = taskDetail.task.priority)
                        }
                    }
                }

            }
        }

        if (showDeleteTaskDialog) {
            DefaultDialog(
                onDismiss = { showDeleteTaskDialog = false },
                buttons = {
                    TextButton(
                        onClick = {
                            scope.launch {
                                dismissState.reset()
                            }
                            showDeleteTaskDialog = false
                        }
                    ) {
                        Text(text = stringResource(R.string.action_cancel))
                    }
                    TextButton(
                        onClick = {
                            scope.launch(Dispatchers.IO) {
                                database.delete(taskDetail.task)
                            }
                        }
                    ) {
                        Text(text = stringResource(R.string.ok))
                    }
                }
            ) {
                Text(text = "Do you want to delete this task ?")
            }
        }
    }


}

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    category: CategoryEntity
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .background(Color(android.graphics.Color.parseColor(category.color)).copy(alpha = 0.24f))
    ) {
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelMedium,
            color = Color(android.graphics.Color.parseColor(category.color)),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(MaterialTheme.padding.small)
        )
    }
}

@Composable
private fun PriorityItem(modifier: Modifier = Modifier, priority: Priority) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                MaterialTheme.shapes.extraSmall
            )
            .padding(MaterialTheme.padding.extraSmall)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_flag_2),
            contentDescription = null
        )
        Text(
            text = priority.name,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
