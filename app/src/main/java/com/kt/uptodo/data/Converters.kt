package com.kt.uptodo.data

import androidx.room.TypeConverter
import com.kt.uptodo.data.enums.Priority
import java.time.LocalDateTime
import java.time.OffsetDateTime

class Converters {

    @TypeConverter
    fun localDateTimeToString(value: LocalDateTime): String {
        return value.toString()
    }

    @TypeConverter
    fun stringToLocalDateTime(value: String): LocalDateTime {
        return LocalDateTime.parse(value)
    }

    @TypeConverter
    fun priorityToString(value: Priority): String {
        return value.name
    }

    @TypeConverter
    fun stringToPriority(value: String): Priority {
        return Priority.valueOf(value)
    }
}