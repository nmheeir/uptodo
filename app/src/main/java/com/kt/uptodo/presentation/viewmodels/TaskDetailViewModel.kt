package com.kt.uptodo.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kt.uptodo.data.entities.CategoryEntity
import com.kt.uptodo.data.enums.Priority
import com.kt.uptodo.data.relations.TaskDetail
import com.kt.uptodo.utils.fakeTaskDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val taskId: Long = savedStateHandle.get<Long>("taskId")!!

    val taskDetail = MutableStateFlow<TaskDetail?>(null)
    val subTask = MutableStateFlow<List<TaskDetail>>(emptyList())

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
                    taskDetail.value!!.copy(task = taskDetail.value!!.task.copy(end = action.time))
            }

            is TaskDetailAction.UpdateTaskTitle -> {
                taskDetail.value =
                    taskDetail.value!!.copy(task = taskDetail.value!!.task.copy(title = action.title))
            }

            is TaskDetailAction.DeleteTask -> {
            }
        }
    }

    init {
        Timber.d(taskId.toString())
        taskDetail.value = fakeTaskDetails.find {
            it.task.taskId == taskId
        }
        subTask.value = fakeTaskDetails.filter {
            it.task.parentTask == taskDetail.value!!.task.taskId
        }
    }
}


sealed interface TaskDetailAction {
    data class UpdateTaskTitle(val title: String) : TaskDetailAction
    data class UpdateTaskTime(val time: OffsetDateTime) : TaskDetailAction
    data class UpdateTaskCategory(val category: CategoryEntity) : TaskDetailAction
    data class UpdateTaskPriority(val priority: Priority) : TaskDetailAction
    data class DeleteTask(val task: TaskDetail) : TaskDetailAction
}

sealed interface TaskDetailEvent {
    data object ShowTaskTimeDialog : TaskDetailEvent
    data object ShowTaskCategoryDialog : TaskDetailEvent
    data object ShowTaskPriorityDialog : TaskDetailEvent
    data object ShowAddSubTaskDialog : TaskDetailEvent
}