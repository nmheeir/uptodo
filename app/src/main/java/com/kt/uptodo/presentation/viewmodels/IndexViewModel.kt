package com.kt.uptodo.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.uptodo.data.UptodoDatabase
import com.kt.uptodo.data.entities.TaskEntity
import com.kt.uptodo.data.enums.Priority
import com.kt.uptodo.data.relations.TaskDetail
import com.kt.uptodo.utils.fakeTaskDetails
import com.kt.uptodo.utils.fakeTasks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class IndexViewModel @Inject constructor(
    private val database: UptodoDatabase
) : ViewModel() {

    val isLoading = MutableStateFlow(false)

    val taskDetails = MutableStateFlow<List<TaskDetail>>(emptyList())
    val uiState = MutableStateFlow(IndexUi())

    fun onAction(action: IndexUiAction) {
        when (action) {
            IndexUiAction.CreateNewTask -> {
                createTask()
            }

            is IndexUiAction.UpdateNewTaskCategory -> {
                uiState.value = uiState.value.copy(
                    newTask = uiState.value.newTask.copy(
                        categoryId = action.categoryId
                    )
                )
            }

            is IndexUiAction.UpdateNewTaskPriority -> {
                uiState.value = uiState.value.copy(
                    newTask = uiState.value.newTask.copy(
                        priority = action.priority
                    )
                )
            }

            is IndexUiAction.UpdateNewTaskDeadline -> {
                uiState.value = uiState.value.copy(
                    newTask = uiState.value.newTask.copy(
                        deadline = action.time
                    )
                )
            }

            is IndexUiAction.UpdateNewTaskTitle -> {
                uiState.value = uiState.value.copy(
                    newTask = uiState.value.newTask.copy(
                        title = action.title
                    )
                )
            }
        }
    }

    private fun createTask() {
        viewModelScope.launch {
            database.insert(uiState.value.newTask)
        }
    }


    init {
        isLoading.value = true
        viewModelScope.launch {
            getTasks()
            isLoading.value = false
        }
    }

    private suspend fun getTasks() {
//        delay(1000)
        uiState.value = uiState.value.copy(
            tasks = fakeTaskDetails
        )
    }
}

sealed interface IndexUiAction {
    data object CreateNewTask : IndexUiAction
    data class UpdateNewTaskTitle(val title: String) : IndexUiAction
    data class UpdateNewTaskDeadline(val time: LocalDateTime) : IndexUiAction
    data class UpdateNewTaskPriority(val priority: Priority) : IndexUiAction
    data class UpdateNewTaskCategory(val categoryId: Long) : IndexUiAction
}

data class IndexUi(
    val tasks: List<TaskDetail> = emptyList(),
    val newTask: TaskEntity = TaskEntity(
        categoryId = 0,
        title = "",
        description = "",
        priority = Priority.LOW,
        deadline = LocalDateTime.now(),
        isReminder = false
    )
)
