package com.kt.uptodo.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.kt.uptodo.data.entities.CategoryEntity
import com.kt.uptodo.data.entities.TaskEntity

data class CategoryWithTasks(
    @Embedded val category: CategoryEntity,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    ) val tasks: List<TaskEntity>
)
