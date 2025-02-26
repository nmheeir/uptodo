package com.kt.uptodo.presentation.screens.index

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.uptodo.R
import com.kt.uptodo.presentation.LocalDatabase
import com.kt.uptodo.presentation.LocalWindowInsets
import com.kt.uptodo.presentation.components.NewTaskBottomSheet
import com.kt.uptodo.presentation.components.SwipeBackground
import com.kt.uptodo.presentation.components.TaskItem
import com.kt.uptodo.presentation.components.dialog.DefaultDialog
import com.kt.uptodo.presentation.shared.NewTaskViewModel
import com.kt.uptodo.presentation.viewmodels.IndexViewModel
import com.kt.uptodo.utils.padding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndexScreen(
    navController: NavHostController,
    viewModel: IndexViewModel = hiltViewModel(),
    newTaskViewModel: NewTaskViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val database = LocalDatabase.current
    val lazyListState = rememberLazyListState()

    val allTasks by viewModel.allTasks.collectAsStateWithLifecycle()
    val newTask by newTaskViewModel.newTask.collectAsStateWithLifecycle()

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

                itemsIndexed(
                    items = tasks,
                    key = { _, task ->
                        task.hashCode()
                    }
                ) { index, task ->
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
                        modifier = Modifier.matchParentSize(),
                        state = dismissState,
                        backgroundContent = {
                            SwipeBackground(
                                dismissState = dismissState,
                                endIcon = R.drawable.ic_delete
                            )
                        },
                        enableDismissFromStartToEnd = false
                    ) {
                        TaskItem(
                            taskDetail = task,
                            onClick = {
                                navController.navigate("task_detail/${task.task.taskId}")
                            }
                        )

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
                                                database.delete(task.task)
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
                action = newTaskViewModel::onAction,
                newTask = newTask
            )
        }
    }
}
