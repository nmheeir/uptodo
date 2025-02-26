package com.kt.uptodo.data

const val TABLE_TASK = "tasks"
const val TABLE_CATEGORY = "categories"

// Định nghĩa tên cột dưới dạng const val
object DbColumn {
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
}
