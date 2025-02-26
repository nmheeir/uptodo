package com.kt.uptodo.presentation.shared

import com.kt.uptodo.data.enums.Priority
import java.time.LocalDateTime

sealed interface NewTaskSheetUiAction {
    data object CreateNewTask : NewTaskSheetUiAction
    data class UpdateNewTaskTitle(val title: String) : NewTaskSheetUiAction
    data class UpdateNewTaskDescription(val description: String) : NewTaskSheetUiAction
    data class UpdateNewTaskDeadline(val time: LocalDateTime) : NewTaskSheetUiAction
    data class UpdateNewTaskPriority(val priority: Priority) : NewTaskSheetUiAction
    data class UpdateNewTaskCategory(val categoryId: Long) : NewTaskSheetUiAction
}