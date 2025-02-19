package com.kt.uptodo.data.enums

enum class Priority {
    LOW, MEDIUM, HIGH;

    companion object {
        fun toList(): List<Priority> {
            return Priority.entries.toList()
        }
    }
}