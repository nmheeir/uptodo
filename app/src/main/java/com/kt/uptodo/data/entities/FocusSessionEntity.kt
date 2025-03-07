package com.kt.uptodo.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "focus_session")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true) val focusSessionId: Int = 0,
    val duration: Long,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
