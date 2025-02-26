package com.kt.uptodo.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.kt.uptodo.data.entities.C_CATEGORY_ID
import com.kt.uptodo.data.entities.CategoryEntity
import com.kt.uptodo.data.entities.TaskEntity

data class TaskDetail(
    @Embedded val task: TaskEntity,

    @Relation(
        parentColumn = C_CATEGORY_ID,
        entityColumn = "categoryId"
    ) val category: CategoryEntity
)
