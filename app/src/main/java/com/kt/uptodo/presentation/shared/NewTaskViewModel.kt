package com.kt.uptodo.presentation.shared

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.uptodo.data.UptodoDatabase
import com.kt.uptodo.utils.DefaultTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewTaskViewModel @Inject constructor(
    private val database: UptodoDatabase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val parentTaskId = savedStateHandle.get<Long>("taskId")

    val newTask = if (parentTaskId == null) {
        MutableStateFlow(DefaultTask)
    } else {
        MutableStateFlow(DefaultTask.copy(parentTask = parentTaskId))
    }

    fun onAction(action: NewTaskSheetUiAction) {
        when (action) {
            NewTaskSheetUiAction.CreateNewTask -> {
                createTask()
            }

            is NewTaskSheetUiAction.UpdateNewTaskCategory -> {
                newTask.value = newTask.value.copy(
                    categoryId = action.categoryId
                )
            }

            is NewTaskSheetUiAction.UpdateNewTaskPriority -> {
                newTask.value = newTask.value.copy(
                    priority = action.priority
                )
            }

            is NewTaskSheetUiAction.UpdateNewTaskDeadline -> {
                newTask.value = newTask.value.copy(
                    deadline = action.time
                )
            }

            is NewTaskSheetUiAction.UpdateNewTaskTitle -> {
                newTask.value = newTask.value.copy(
                    title = action.title
                )
            }

            is NewTaskSheetUiAction.UpdateNewTaskDescription -> {
                newTask.value = newTask.value.copy(
                    description = action.description
                )
            }
        }
    }

    private fun createTask() {
        viewModelScope.launch {
            database.insert(newTask.value)
        }
    }
}
