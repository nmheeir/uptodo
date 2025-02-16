package com.kt.uptodo.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.uptodo.R
import com.kt.uptodo.data.relations.TaskDetail
import com.kt.uptodo.presentation.LocalWindowInsets
import com.kt.uptodo.presentation.components.TaskItem
import com.kt.uptodo.presentation.theme.UpTodoTheme
import com.kt.uptodo.presentation.viewmodels.IndexViewModel
import com.kt.uptodo.utils.fakeTaskDetails
import com.kt.uptodo.utils.padding
import kotlinx.collections.immutable.persistentListOf

@Composable
fun IndexScreen(
    navController: NavHostController,
    viewModel: IndexViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val taskDetails by viewModel.taskDetails.collectAsStateWithLifecycle()


    when {
        taskDetails.isEmpty() -> {
            EmptyScreen(
                R.string.empty_description,
                actions = persistentListOf(
                    EmptyScreenAction(
                        stringRes = R.string.action_add_task,
                        icon = R.drawable.ic_add_box,
                        onClick = { /*TODO*/ }
                    )
                )
            )
        }

        else -> {
            IndexContent(
                taskDetails = taskDetails
            )
        }
    }
}

@Composable
private fun IndexContent(
    modifier: Modifier = Modifier,
    taskDetails: List<TaskDetail>
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
        contentPadding = LocalWindowInsets.current.asPaddingValues(),
        modifier = modifier.padding(horizontal = MaterialTheme.padding.mediumSmall)
    ) {
        taskDetails.takeIf { it.isNotEmpty() }?.let {
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
                items = taskDetails,
                key = { it.hashCode() }
            ) { taskDetail ->
                TaskItem(
                    taskDetail = taskDetail,
                    onClick = {

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
}