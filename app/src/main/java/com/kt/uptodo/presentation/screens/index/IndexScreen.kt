package com.kt.uptodo.presentation.screens.index

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.kt.uptodo.core.TriState
import com.kt.uptodo.presentation.LocalWindowInsets
import com.kt.uptodo.presentation.components.NewTaskBottomSheet
import com.kt.uptodo.presentation.components.TabbedSheet
import com.kt.uptodo.presentation.components.TaskItem
import com.kt.uptodo.presentation.components.TriStateItem
import com.kt.uptodo.presentation.screens.EmptyScreen
import com.kt.uptodo.presentation.screens.EmptyScreenAction
import com.kt.uptodo.presentation.shared.NewTaskViewModel
import com.kt.uptodo.presentation.viewmodels.IndexViewModel
import com.kt.uptodo.utils.padding
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndexScreen(
    navController: NavHostController,
    viewModel: IndexViewModel = hiltViewModel(),
    newTaskViewModel: NewTaskViewModel = hiltViewModel()
) {
    val lazyListState = rememberLazyListState()
    val allTasks by viewModel.allTasks.collectAsStateWithLifecycle()
    val newTask by newTaskViewModel.newTask.collectAsStateWithLifecycle()

    var showFilterSheet by remember { mutableStateOf(false) }
    var showNewTaskBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            IndexTopBar(
                onClick = {
                    showFilterSheet = true
                }
            )
        },
        modifier = Modifier
            .padding(LocalWindowInsets.current.asPaddingValues())
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            if (allTasks.isEmpty()) {
                EmptyScreen(
                    modifier = Modifier.fillMaxSize(),
                    stringRes = R.string.empty_description,
                    actions = persistentListOf(
                        EmptyScreenAction(
                            stringRes = R.string.action_add_task,
                            icon = R.drawable.ic_add_box,
                            onClick = { showNewTaskBottomSheet = true }
                        )
                    )
                )
            }

            LazyColumn(
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
                modifier = Modifier
                    .align(Alignment.TopStart)
                .padding(horizontal = MaterialTheme.padding.mediumSmall)
            ) {
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
                    ) { _, task ->
                        TaskItem(
                            taskDetail = task,
                            onClick = {
                                navController.navigate("task_detail/${task.task.taskId}")
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

            FloatingActionButton(
                onClick = {
                    showNewTaskBottomSheet = true
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(MaterialTheme.padding.mediumSmall)
            ) {
                Icon(painterResource(R.drawable.ic_add_box), null)
            }

            if (showNewTaskBottomSheet) {
                NewTaskBottomSheet(
                    onDismiss = { showNewTaskBottomSheet = false },
                    action = newTaskViewModel::onAction,
                    newTask = newTask
                )
            }
            if (showFilterSheet) {
                TabbedSheet(
                    onDismissRequest = { showFilterSheet = false },
                    tabTitles = persistentListOf(
                        "con cac",
                        "dau boi"
                    )
                ) { page ->
                    when (page) {
                        0 -> Text(text = "con cac")
                        1 -> Text(text = "dau boi")
                    }
                }
            }
        }
    }
}

@Composable
private fun IndexTopBar(
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.medium),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = onClick
        ) {
            Icon(painterResource(R.drawable.ic_filter_list), null)
        }

        Text(
            text = "Index",
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun ColumnScope.FilterTask(modifier: Modifier = Modifier) {
    TriStateItem(
        label = stringResource(R.string.label_complete),
        state = TriState.DISABLED
    ) { }
}

@Composable
private fun SortTask(modifier: Modifier = Modifier) {

}
