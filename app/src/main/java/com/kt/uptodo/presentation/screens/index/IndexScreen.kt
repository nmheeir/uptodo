package com.kt.uptodo.presentation.screens.index

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.uptodo.R
import com.kt.uptodo.data.entities.TaskEntity
import com.kt.uptodo.presentation.LocalWindowInsets
import com.kt.uptodo.presentation.components.TaskItem
import com.kt.uptodo.presentation.components.dialog.CategoryDialog
import com.kt.uptodo.presentation.components.dialog.PriorityDialog
import com.kt.uptodo.presentation.components.dialog.TaskTimeDialog
import com.kt.uptodo.presentation.navigation.Screens
import com.kt.uptodo.presentation.viewmodels.IndexUiAction
import com.kt.uptodo.presentation.viewmodels.IndexViewModel
import com.kt.uptodo.utils.padding
import kotlinx.coroutines.delay

@Composable
fun IndexScreen(
    navController: NavHostController,
    viewModel: IndexViewModel = hiltViewModel()
) {
    val lazyListState = rememberLazyListState()

    val allTasks by viewModel.allTasks.collectAsStateWithLifecycle()
    val newTask by viewModel.newTask.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(LocalWindowInsets.current.asPaddingValues())
    ) {
        LazyColumn(
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(horizontal = MaterialTheme.padding.mediumSmall)
        ) {
            item {
                if (allTasks.isEmpty()) {
                    Text(
                        text = "is empty"
                    )
                }
            }
            allTasks.takeIf { it.isNotEmpty() }?.let { tasks ->
                item {
                    Text(
                        text = stringResource(R.string.label_today),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(
                                horizontal = MaterialTheme.padding.medium,
                                vertical = MaterialTheme.padding.small
                            )
                    )
                }

                items(
                    items = tasks,
                    key = { it.hashCode() }
                ) { taskDetail ->
                    TaskItem(
                        taskDetail = taskDetail,
                        onClick = {
                            navController.navigate("task_detail/${taskDetail.task.taskId}")
                        }
                    )
                }

                item {
                    Text(
                        text = stringResource(R.string.label_complete),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(
                                horizontal = MaterialTheme.padding.medium,
                                vertical = MaterialTheme.padding.small
                            )
                    )
                }
            }
        }

        var showBottomSheet by remember { mutableStateOf(false) }
        FloatingActionButton(
            onClick = {
                showBottomSheet = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(MaterialTheme.padding.mediumSmall)
        ) {
            Icon(painterResource(R.drawable.ic_add_box), null)
        }

        if (showBottomSheet) {
            NewTaskBottomSheet(
                onDismiss = { showBottomSheet = false },
                action = viewModel::onAction,
                newTask = newTask
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewTaskBottomSheet(
    onDismiss: () -> Unit,
    action: (IndexUiAction) -> Unit,
    newTask: TaskEntity
) {
    val focusManager = LocalFocusManager.current
    val (focus1, focus2) = remember { FocusRequester.createRefs() }

    var showTaskTime by remember { mutableStateOf(false) }
    var showTaskPriority by remember { mutableStateOf(false) }
    var showTaskCategory by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        focus2.requestFocus()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .padding(
                bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
            )

    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.mediumSmall),
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.padding.mediumSmall)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                        }
                    )
                }
        ) {
            Text(
                text = "Add task",
                style = MaterialTheme.typography.labelLarge
            )

            val (title, onTitleChange) = remember { mutableStateOf(newTask.title) }
            OutlinedTextField(
                value = title,
                onValueChange = {
                    onTitleChange(it)
                    action(IndexUiAction.UpdateNewTaskTitle(it))
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focus2.requestFocus() }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focus1)
            )

            val (description, onDescriptionChange) = remember { mutableStateOf(newTask.description) }
            OutlinedTextField(
                value = description,
                onValueChange = {
                    onDescriptionChange(it)
                    action(IndexUiAction.UpdateNewTaskTitle(it))
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focus2)

            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
                ) {
                    //Time
                    IconButton(
                        onClick = { showTaskTime = true }
                    ) {
                        Icon(painterResource(R.drawable.ic_timer), null)
                    }

                    // Category
                    IconButton(
                        onClick = { showTaskCategory = true }
                    ) {
                        Icon(painterResource(R.drawable.ic_sell), null)
                    }

                    //Priority
                    IconButton(
                        onClick = { showTaskPriority = true }
                    ) {
                        Icon(painterResource(R.drawable.ic_flag_2), null)
                    }
                }

                IconButton(
                    onClick = {
//                        action(IndexUiAction.CreateNewTask)
                        onDismiss()
                    }
                ) {
                    Icon(painterResource(R.drawable.ic_send), null)
                }
            }
        }
    }

    if (showTaskTime) {
        TaskTimeDialog(
            taskDateTime = newTask.deadline,
            onDismiss = { showTaskTime = false },
            onTaskDateTimeChange = {
                action(IndexUiAction.UpdateNewTaskDeadline(it))
                showTaskCategory = false
            }
        )
    }

    if (showTaskPriority) {
        PriorityDialog(
            selectedPriority = newTask.priority,
            onValueSelected = {
                action(IndexUiAction.UpdateNewTaskPriority(it))
                showTaskPriority = false
            },
            onDismiss = { showTaskPriority = false }
        )
    }

    if (showTaskCategory) {
        CategoryDialog(
            onValueSelected = {
                action(IndexUiAction.UpdateNewTaskCategory(it.categoryId))
                showTaskCategory = false
            },
            onDismiss = { showTaskCategory = false }
        )
    }
}