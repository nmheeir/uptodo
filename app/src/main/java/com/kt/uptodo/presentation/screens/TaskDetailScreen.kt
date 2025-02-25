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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.uptodo.R
import com.kt.uptodo.data.entities.TaskEntity
import com.kt.uptodo.data.relations.TaskDetail
import com.kt.uptodo.extensions.convertToDeadline
import com.kt.uptodo.presentation.LocalWindowInsets
import com.kt.uptodo.presentation.components.TaskItem
import com.kt.uptodo.presentation.components.dialog.CategoryDialog
import com.kt.uptodo.presentation.components.dialog.DefaultDialog
import com.kt.uptodo.presentation.components.dialog.PriorityDialog
import com.kt.uptodo.presentation.components.dialog.TaskTimeDialog
import com.kt.uptodo.presentation.components.dialog.TextFieldDialog
import com.kt.uptodo.presentation.theme.UpTodoTheme
import com.kt.uptodo.presentation.viewmodels.TaskDetailAction
import com.kt.uptodo.presentation.viewmodels.ShowDialogEvent
import com.kt.uptodo.presentation.viewmodels.TaskDetailViewModel
import com.kt.uptodo.utils.Gap
import com.kt.uptodo.utils.fakeCategories
import com.kt.uptodo.utils.fakeTaskDetails
import com.kt.uptodo.utils.padding

@Composable
fun TaskDetailScreen(
    navController: NavHostController,
    viewModel: TaskDetailViewModel = hiltViewModel()
) {
    val task by viewModel.taskDetail.collectAsStateWithLifecycle()
    val subTask by viewModel.subTask.collectAsStateWithLifecycle()

    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
            .fillMaxSize()
            .padding(LocalWindowInsets.current.asPaddingValues())
            .padding(horizontal = MaterialTheme.padding.mediumSmall)
    ) {
        TaskDetailScreenContent(
            navController = navController,
            taskDetail = task!!,
            subTask = subTask,
            action = viewModel::onAction,
        )
    }
}

@Composable
private fun TaskDetailScreenContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    taskDetail: TaskDetail,
    subTask: List<TaskDetail>,
    action: (TaskDetailAction) -> Unit,
) {
    var showEditTaskTitleDialog by remember { mutableStateOf(false) }
    var showTaskTimeDialog by remember { mutableStateOf(false) }
    var showTaskCategoryDialog by remember { mutableStateOf(false) }
    var showTaskPriorityDialog by remember { mutableStateOf(false) }
    var showAddSubTaskDialog by remember { mutableStateOf(false) }
    var showDeleteTaskDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
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

            IconButton(
                onClick = { showEditTaskTitleDialog = true }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_border_color),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Gap(height = MaterialTheme.padding.medium)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            TaskDetailContent(
                taskDetail = taskDetail,
                subTask = subTask,
                showDialog = {
                    when (it) {
                        ShowDialogEvent.ShowAddSubTaskDialog -> {
                            showAddSubTaskDialog = true
                        }

                        ShowDialogEvent.ShowTaskCategoryDialog -> {
                            showTaskCategoryDialog = true
                        }

                        ShowDialogEvent.ShowTaskPriorityDialog -> {
                            showTaskPriorityDialog = true
                        }

                        ShowDialogEvent.ShowTaskTimeDialog -> {
                            showTaskTimeDialog = true
                        }

                        ShowDialogEvent.ShowDeleteTaskDialog -> {
                            showDeleteTaskDialog = true
                        }
                    }
                }
            )
        }

        Button(
            shape = MaterialTheme.shapes.extraSmall,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.action_complete_task),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }

    if (showEditTaskTitleDialog) {
        TextFieldDialog(
            title = {
                Text(
                    text = stringResource(R.string.label_edit_task_title)
                )
            },
            initialTextFieldValue = TextFieldValue(
                text = taskDetail.task.title,
                selection = TextRange(taskDetail.task.title.length)
            ),
            onDone = {
                action(TaskDetailAction.UpdateTaskTitle(it))
                showEditTaskTitleDialog = false
            },
            onDismiss = { showEditTaskTitleDialog = false }
        )
    }

    if (showTaskTimeDialog) {
        TaskTimeDialog(
            taskDateTime = taskDetail.task.deadline,
            onDismiss = { showTaskTimeDialog = false },
            onTaskDateTimeChange = {
                action(TaskDetailAction.UpdateTaskTime(it))
            }
        )
    }

    if (showTaskPriorityDialog) {
        PriorityDialog(
            selectedPriority = taskDetail.task.priority,
            onValueSelected = {
                action(TaskDetailAction.UpdateTaskPriority(it))
                showTaskPriorityDialog = false
            },
            onDismiss = {
                showTaskPriorityDialog = false
            }
        )
    }

    if (showAddSubTaskDialog) {

    }

    if (showTaskCategoryDialog) {
        CategoryDialog(
            categories = fakeCategories,
            selectedCategory = taskDetail.category,
            onValueSelected = {
                action(TaskDetailAction.UpdateTaskCategory(it))
            },
            onDismiss = { showTaskCategoryDialog = false },
            onCreateNewCategory = {
                showTaskCategoryDialog = false
                navController.navigate("create_new_category")
            }
        )
    }

    if (showDeleteTaskDialog) {
        DefaultDialog(
            onDismiss = { showDeleteTaskDialog = false },
            icon = {
                Icon(painterResource(R.drawable.ic_delete), null)
            },
            buttons = {
                TextButton(onClick = { showDeleteTaskDialog = false }) {
                    Text(text = stringResource(android.R.string.cancel))
                }
                TextButton(
                    onClick = { action(TaskDetailAction.DeleteTask(taskDetail)) }
                ) {
                    Text(text = stringResource(R.string.action_delete_task))
                }
            }
        ) {

        }
    }
}

@Composable
private fun TaskDetailContent(
    modifier: Modifier = Modifier,
    taskDetail: TaskDetail,
    subTask: List<TaskDetail>,
    showDialog: (ShowDialogEvent) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        TaskDetailHeader(task = taskDetail.task)

        TaskDetailRow(
            icon = R.drawable.ic_timer,
            title = stringResource(id = R.string.label_task_time),
            content = taskDetail.task.deadline.convertToDeadline(),
            onClick = { showDialog(ShowDialogEvent.ShowTaskTimeDialog) }
        )

        TaskDetailRow(
            icon = R.drawable.ic_sell,
            title = stringResource(R.string.label_task_category),
            content = taskDetail.category.name,
            containerColor = Color(android.graphics.Color.parseColor(taskDetail.category.color)).copy(
                alpha = 0.32f
            ),
            contentColor = Color(android.graphics.Color.parseColor(taskDetail.category.color)),
            onClick = { showDialog(ShowDialogEvent.ShowTaskCategoryDialog) }
        )

        TaskDetailRow(
            icon = R.drawable.ic_flag_2,
            title = stringResource(R.string.label_task_priority),
            content = taskDetail.task.priority.name,
            onClick = { showDialog(ShowDialogEvent.ShowTaskPriorityDialog) }
        )

        TaskDetailRow(
            icon = R.drawable.ic_graph_1,
            title = stringResource(R.string.label_sub_task),
            content = stringResource(R.string.action_add_subtask),
            onClick = { showDialog(ShowDialogEvent.ShowAddSubTaskDialog) }
        )

        if (subTask.isNotEmpty()) {
            subTask.fastForEach {
                TaskItem(
                    taskDetail = it,
                    modifier = Modifier.padding(start = MaterialTheme.padding.medium)
                )
            }
        }

        TextButton(
            onClick = {}
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.medium),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Text(
                    text = stringResource(R.string.action_delete_task),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = task.title
            )
            TaskDetailDescription(task.description)
        }

        IconButton(
            onClick = {}
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_border_color),
                contentDescription = null
            )
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


@Preview(showBackground = true)
@Composable
private fun Test() {
    UpTodoTheme {
        TaskDetailContent(
            taskDetail = fakeTaskDetails[1],
            subTask = fakeTaskDetails.take(2),
            showDialog = {}
        )
    }
}
