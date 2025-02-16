package com.kt.uptodo.data

import androidx.room.TypeConverter
import com.kt.uptodo.data.enums.Priority
import java.time.OffsetDateTime

class Converters {

    @TypeConverter
    fun offsetDataTimeToString(value: OffsetDateTime): String {
        return value.toString()
    }

    @TypeConverter
    fun stringToOffsetDateTime(value: String): OffsetDateTime {
        return OffsetDateTime.parse(value)
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