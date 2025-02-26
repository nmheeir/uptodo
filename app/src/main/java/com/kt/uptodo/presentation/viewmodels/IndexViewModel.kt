package com.kt.uptodo.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.uptodo.data.UptodoDatabase
import com.kt.uptodo.data.enums.Priority
import com.kt.uptodo.utils.DefaultTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class IndexViewModel @Inject constructor(
    private val database: UptodoDatabase
) : ViewModel() {

    val isLoading = MutableStateFlow(false)

    val allTasks = database.task()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val newTask = MutableStateFlow(DefaultTask)

    fun onAction(action: IndexUiAction) {
        when (action) {
            IndexUiAction.CreateNewTask -> {
                createTask()
            }

            is IndexUiAction.UpdateNewTaskCategory -> {
                newTask.value = newTask.value.copy(
                    categoryId = action.categoryId
                )
            }

            is IndexUiAction.UpdateNewTaskPriority -> {
                newTask.value = newTask.value.copy(
                    priority = action.priority
                )
            }

            is IndexUiAction.UpdateNewTaskDeadline -> {
                newTask.value = newTask.value.copy(
                    deadline = action.time
                )
            }

            is IndexUiAction.UpdateNewTaskTitle -> {
                newTask.value = newTask.value.copy(
                    title = action.title
                )
            }

            is IndexUiAction.UpdateNewTaskDescription -> {
                newTask.value = newTask.value.copy(
                    description = action.description
                )
            }
        }
    }

    private fun createTask() {
        viewModelScope.launch(Dispatchers.IO) {
            database.insert(newTask.value)
        }
    }
}

sealed interface IndexUiAction {
    data object CreateNewTask : IndexUiAction
    data class UpdateNewTaskTitle(val title: String) : IndexUiAction
    data class UpdateNewTaskDescription(val description: String) : IndexUiAction
    data class UpdateNewTaskDeadline(val time: LocalDateTime) : IndexUiAction
    data class UpdateNewTaskPriority(val priority: Priority) : IndexUiAction
    data class UpdateNewTaskCategory(val categoryId: Long) : IndexUiAction
}