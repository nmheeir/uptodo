package com.kt.uptodo.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.uptodo.data.UptodoDatabase
import com.kt.uptodo.data.entities.CategoryEntity
import com.kt.uptodo.data.enums.Priority
import com.kt.uptodo.data.relations.TaskDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val database: UptodoDatabase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val taskId: Long = savedStateHandle.get<Long>("taskId")!!

    val taskDetail = database.task(taskId)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val subTask = database.subTasks(taskId)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun onAction(action: TaskDetailAction) {
        viewModelScope.launch {
            when (action) {
                is TaskDetailAction.UpdateTaskCategory -> {
                    database.update(taskDetail.value!!.task.copy(categoryId = action.category.categoryId))
                }

                is TaskDetailAction.UpdateTaskPriority -> {
                    database.update(taskDetail.value!!.task.copy(priority = action.priority))
                }

                is TaskDetailAction.UpdateTaskTime -> {
                    database.update(taskDetail.value!!.task.copy(deadline = action.time))
                }

                is TaskDetailAction.UpdateTaskTitle -> {
                    database.update(taskDetail.value!!.task.copy(title = action.title))
                }

                is TaskDetailAction.DeleteTask -> {
                    deleteTask()
                }

                TaskDetailAction.CompleteTask -> {
                    completeTask()
                }
            }
        }
    }

    private suspend fun completeTask() {
        database.update(taskDetail.value!!.task.copy(isComplete = true))
    }

    private suspend fun deleteTask() {
        database.delete(taskDetail.value!!.task)
    }
}

sealed interface TaskDetailAction {
    data class UpdateTaskTitle(val title: String) : TaskDetailAction
    data class UpdateTaskTime(val time: LocalDateTime) : TaskDetailAction
    data class UpdateTaskCategory(val category: CategoryEntity) : TaskDetailAction
    data class UpdateTaskPriority(val priority: Priority) : TaskDetailAction
    data class DeleteTask(val task: TaskDetail) : TaskDetailAction
    data object CompleteTask : TaskDetailAction
}

sealed interface ShowDialogEvent {
    data object ShowTaskTimeDialog : ShowDialogEvent
    data object ShowTaskCategoryDialog : ShowDialogEvent
    data object ShowTaskPriorityDialog : ShowDialogEvent
    data object ShowAddSubTaskDialog : ShowDialogEvent
    data object ShowDeleteTaskDialog : ShowDialogEvent
}