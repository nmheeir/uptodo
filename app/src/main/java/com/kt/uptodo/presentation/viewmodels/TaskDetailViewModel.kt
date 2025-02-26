package com.kt.uptodo.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kt.uptodo.data.UptodoDatabase
import com.kt.uptodo.data.entities.CategoryEntity
import com.kt.uptodo.data.enums.Priority
import com.kt.uptodo.data.relations.TaskDetail
import com.kt.uptodo.presentation.shared.NewTaskSheetUiAction
import com.kt.uptodo.utils.fakeTaskDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val database: UptodoDatabase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val taskId: Long = savedStateHandle.get<Long>("taskId")!!

    val taskDetail = MutableStateFlow<TaskDetail?>(null)
    val subTask = database.subTasks(taskId)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _channel = Channel<ShowDialogEvent>()
    val channel = _channel.receiveAsFlow()

    fun onAction(action: TaskDetailAction) {
        when (action) {
            is TaskDetailAction.UpdateTaskCategory -> {
                taskDetail.value = taskDetail.value!!.copy(category = action.category)
            }

            is TaskDetailAction.UpdateTaskPriority -> {
                taskDetail.value =
                    taskDetail.value!!.copy(task = taskDetail.value!!.task.copy(priority = action.priority))
            }

            is TaskDetailAction.UpdateTaskTime -> {
                taskDetail.value =
                    taskDetail.value!!.copy(task = taskDetail.value!!.task.copy(deadline = action.time))
            }

            is TaskDetailAction.UpdateTaskTitle -> {
                taskDetail.value =
                    taskDetail.value!!.copy(task = taskDetail.value!!.task.copy(title = action.title))
            }

            is TaskDetailAction.DeleteTask -> {
                deleteTask()
            }
        }
    }

    init {
        Timber.d(taskId.toString())
        viewModelScope.launch {
            taskDetail.value = database.task(taskId)
        }
    }

    private fun deleteTask() {
        viewModelScope.launch {
//            database.delete()
        }
    }
}

sealed interface TaskDetailAction {
    data class UpdateTaskTitle(val title: String) : TaskDetailAction
    data class UpdateTaskTime(val time: LocalDateTime) : TaskDetailAction
    data class UpdateTaskCategory(val category: CategoryEntity) : TaskDetailAction
    data class UpdateTaskPriority(val priority: Priority) : TaskDetailAction
    data class DeleteTask(val task: TaskDetail) : TaskDetailAction
}

sealed interface ShowDialogEvent {
    data object ShowTaskTimeDialog : ShowDialogEvent
    data object ShowTaskCategoryDialog : ShowDialogEvent
    data object ShowTaskPriorityDialog : ShowDialogEvent
    data object ShowAddSubTaskDialog : ShowDialogEvent
    data object ShowDeleteTaskDialog : ShowDialogEvent
}