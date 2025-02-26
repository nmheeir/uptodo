package com.kt.uptodo.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kt.uptodo.data.TABLE_CATEGORY

@Entity(tableName = TABLE_CATEGORY)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val categoryId: Long = 0,
    val name: String,
    val color: String
)