package com.kt.uptodo.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kt.uptodo.data.enums.Priority
import java.time.OffsetDateTime

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val taskId: Long = 0,
    val parentTask: Long? = null,
    val categoryId: Long = 0,
    val title: String,
    val description: String,
    val priority: Priority,
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    val updatedAt: OffsetDateTime? = null,
    val isComplete: Boolean = false,
    val isReminder: Boolean,
    val start: OffsetDateTime,
    val end: OffsetDateTime
)
