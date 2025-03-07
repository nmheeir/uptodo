package com.kt.uptodo.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kt.uptodo.data.TABLE_TASK
import com.kt.uptodo.data.enums.Priority
import java.time.LocalDateTime

@Entity(tableName = TABLE_TASK)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = C_TASK_ID) val taskId: Long = 0,
    @ColumnInfo(name = C_PARENT_TASK) val parentTask: Long? = null,
    @ColumnInfo(name = C_CATEGORY_ID) val categoryId: Long = 0,
    @ColumnInfo(name = C_TITLE) val title: String,
    @ColumnInfo(name = C_DESCRIPTION) val description: String,
    @ColumnInfo(name = C_PRIORITY) val priority: Priority,
    @ColumnInfo(name = C_CREATED_AT) val createdAt: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = C_UPDATED_AT) val updatedAt: LocalDateTime? = null,
    @ColumnInfo(name = C_IS_COMPLETE) val isComplete: Boolean = false,
    @ColumnInfo(name = C_IS_REMINDER) val isReminder: Boolean,
    @ColumnInfo(name = C_DURATION) val duration: Long = 0,
    @ColumnInfo(name = C_DEADLINE) val deadline: LocalDateTime
)

const val C_TASK_ID = "task_id"
const val C_PARENT_TASK = "parent_task"
const val C_CATEGORY_ID = "category_id"
const val C_TITLE = "title"
const val C_DESCRIPTION = "description"
const val C_PRIORITY = "priority"
const val C_CREATED_AT = "created_at"
const val C_UPDATED_AT = "updated_at"
const val C_IS_COMPLETE = "is_complete"
const val C_IS_REMINDER = "is_reminder"
const val C_DEADLINE = "deadline"
const val C_DURATION = "duration"
