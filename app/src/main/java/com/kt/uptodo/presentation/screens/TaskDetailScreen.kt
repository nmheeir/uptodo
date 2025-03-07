package com.kt.uptodo.presentation.screens

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.uptodo.R
import com.kt.uptodo.data.entities.TaskEntity
import com.kt.uptodo.data.relations.TaskDetail
import com.kt.uptodo.extensions.parseColor
import com.kt.uptodo.extensions.parseDate
import com.kt.uptodo.extensions.parseToTime
import com.kt.uptodo.presentation.LocalDatabase
import com.kt.uptodo.presentation.LocalWindowInsets
import com.kt.uptodo.presentation.components.NewTaskBottomSheet
import com.kt.uptodo.presentation.components.TaskItem
import com.kt.uptodo.presentation.components.dialog.CategoryDialog
import com.kt.uptodo.presentation.components.dialog.DefaultDialog
import com.kt.uptodo.presentation.components.dialog.DurationDialog
import com.kt.uptodo.presentation.components.dialog.PriorityDialog
import com.kt.uptodo.presentation.components.dialog.TaskTimeDialog
import com.kt.uptodo.presentation.components.dialog.TextFieldDialog
import com.kt.uptodo.presentation.shared.NewTaskSheetUiAction
import com.kt.uptodo.presentation.shared.NewTaskViewModel
import com.kt.uptodo.presentation.theme.UpTodoTheme
import com.kt.uptodo.presentation.viewmodels.ShowDialogEvent
import com.kt.uptodo.presentation.viewmodels.TaskDetailAction
import com.kt.uptodo.presentation.viewmodels.TaskDetailViewModel
import com.kt.uptodo.utils.Gap
import com.kt.uptodo.utils.fakeTaskDetails
import com.kt.uptodo.utils.padding
import kotlinx.coroutines.delay

@Composable
fun TaskDetailScreen(
    navController: NavHostController,
    viewModel: TaskDetailViewModel = hiltViewModel(),
    newTaskViewModel: NewTaskViewModel = hiltViewModel()
) {
    val database = LocalDatabase.current
    val taskDetail by viewModel.taskDetail.collectAsStateWithLifecycle()
    val subTask by viewModel.subTask.collectAsStateWithLifecycle()
    val newTask by newTaskViewModel.newTask.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = navController::navigateUp
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        },
        modifier = Modifier
            .padding(LocalWindowInsets.current.asPaddingValues())
    ) { contentPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = MaterialTheme.padding.mediumSmall)
        ) {
            taskDetail.takeIf { it != null }?.let { task ->

                item {
                    TaskDetailHeader(task = task.task)
                }

                item {
                    var showDurationDialog by remember { mutableStateOf(false) }
                    TaskDetailRow(
                        icon = R.drawable.ic_timer,
                        title = stringResource(R.string.label_duration),
                        content = task.task.duration.parseToTime(),
                        onClick = { showDurationDialog = true }
                    )
                    if (showDurationDialog) {
                        DurationDialog(
                            onDismiss = { showDurationDialog = false }
                        )
                    }
                }

                item {
                    var showTimerDialog by remember { mutableStateOf(false) }
                    TaskDetailRow(
                        icon = R.drawable.ic_calendar_clock,
                        title = stringResource(id = R.string.label_deadline),
                        content = task.task.deadline.parseDate(),
                        onClick = { showTimerDialog = true }
                    )
                    if (showTimerDialog) {
                        TaskTimeDialog(
                            taskDateTime = task.task.deadline,
                            onDismiss = { showTimerDialog = false },
                            onTaskDateTimeChange = {
                                showTimerDialog = false
                            }
                        )
                    }
                }

                item {
                    var showCategoryDialog by remember { mutableStateOf(false) }
                    TaskDetailRow(
                        icon = R.drawable.ic_sell,
                        title = stringResource(R.string.label_task_category),
                        content = task.category.name,
                        containerColor = Color.parseColor(task.category.color).copy(
                            alpha = 0.32f
                        ),
                        contentColor = Color.parseColor(task.category.color),
                        onClick = { showCategoryDialog = true }
                    )
                    if (showCategoryDialog) {
                        CategoryDialog(
                            selectedCategoryId = task.category.categoryId,
                            onValueSelected = {

                            },
                            onDismiss = { showCategoryDialog = false }
                        )
                    }
                }

                item {
                    var showPriorityDialog by remember { mutableStateOf(false) }
                    TaskDetailRow(
                        icon = R.drawable.ic_flag_2,
                        title = stringResource(R.string.label_task_priority),
                        content = task.task.priority.name,
                        onClick = { showPriorityDialog = true }
                    )
                    if (showPriorityDialog) {
                        PriorityDialog(
                            selectedPriority = task.task.priority,
                            onValueSelected = {
                                showPriorityDialog = false
                            },
                            onDismiss = {
                                showPriorityDialog = false
                            }
                        )
                    }
                }

                item {
                    var showCreateSubTaskDialog by remember { mutableStateOf(false) }
                    TaskDetailRow(
                        icon = R.drawable.ic_graph_1,
                        title = stringResource(R.string.label_sub_task),
                        content = stringResource(R.string.action_add_subtask),
                        onClick = { showCreateSubTaskDialog = true }
                    )
                    if (showCreateSubTaskDialog) {
                        NewTaskBottomSheet(
                            onDismiss = { showCreateSubTaskDialog = false },
                            action = {},
                            newTask = newTask
                        )
                    }
                }

                item {
                    Row {
                        var showDeleteTaskDialog by remember { mutableStateOf(false) }
                        TextButton(
                            onClick = { showDeleteTaskDialog = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.action_delete_task)
                            )
                            if (showDeleteTaskDialog) {
                                DefaultDialog(
                                    onDismiss = { showDeleteTaskDialog = true },
                                    buttons = {
                                        TextButton(onClick = { showDeleteTaskDialog = false }) {
                                            Text(text = stringResource(android.R.string.cancel))
                                        }
                                        TextButton(
                                            onClick = {
                                                database.query {
                                                    delete(task.task)
                                                }
                                                navController.navigateUp()
                                            }
                                        ) {
                                            Text(text = stringResource(R.string.action_delete_task))
                                        }
                                    }
                                ) {
                                    Text(
                                        text = "Do you want to delete this task"
                                    )
                                }
                            }
                        }
                        TextButton(
                            onClick = {
                                database.query {
                                    update(task.task.copy(isComplete = true))
                                }
                                navController.navigateUp()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.action_complete_task)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskDetailRow(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    title: String,
    content: String,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                colors = ButtonDefaults.textButtonColors(
                    contentColor = contentColor,
                    containerColor = containerColor
                ),
                shape = MaterialTheme.shapes.medium,
                onClick = onClick
            ) {
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
private fun TaskDetailHeader(
    modifier: Modifier = Modifier,
    task: TaskEntity
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
        modifier = modifier
    ) {
        IconButton(
            onClick = {}
        ) {
            Icon(
                painter = painterResource(
                    if (task.isComplete) R.drawable.ic_check_circle_fill else R.drawable.ic_check_circle
                ),
                contentDescription = null
            )
        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = task.title
            )
            TaskDetailDescription(task.description)
        }

        var showEditTaskTitleDialog by remember { mutableStateOf(false) }
        IconButton(
            onClick = { showEditTaskTitleDialog = true }
        ) {
            val database = LocalDatabase.current
            Icon(
                painter = painterResource(R.drawable.ic_border_color),
                contentDescription = null
            )
            if (showEditTaskTitleDialog) {
                TextFieldDialog(
                    title = {
                        Text(
                            text = stringResource(R.string.label_edit_task_title)
                        )
                    },
                    initialTextFieldValue = TextFieldValue(
                        text = task.title,
                        selection = TextRange(task.title.length)
                    ),
                    onDone = {
                        database.query {
                            update(task.copy(title = it))
                        }
                        showEditTaskTitleDialog = false
                    },
                    onDismiss = { showEditTaskTitleDialog = false }
                )
            }
        }
    }
}

@Composable
private fun TaskDetailDescription(
    description: String
) {
    var isExpanded by remember { mutableStateOf(false) }
    var showSeeMore by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
//            .padding(horizontal = MaterialTheme.padding.medium)
            .clickable { isExpanded = !isExpanded }
    ) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = if (isExpanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { result ->
                showSeeMore = result.hasVisualOverflow
            },
            modifier = Modifier.animateContentSize(
                animationSpec = tween(
                    durationMillis = 200,
                    easing = EaseOutExpo
                )
            )
        )
        if (showSeeMore) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                // TODO: Add gradient effect
                Text(
                    text = stringResource(id = R.string.label_see_more),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}
