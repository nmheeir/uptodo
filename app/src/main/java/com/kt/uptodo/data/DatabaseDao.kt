package com.kt.uptodo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.kt.uptodo.data.entities.CategoryEntity
import com.kt.uptodo.data.entities.TaskEntity
import com.kt.uptodo.data.relations.TaskDetail

@Dao
interface DatabaseDao {

    /*Insert*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: CategoryEntity)

    /*Update*/
    @Update
    fun update(task: TaskEntity)

    @Update
    fun update(category: CategoryEntity)

    /*Delete*/
    @Delete
    fun delete(task: TaskEntity)

    @Delete
    fun delete(category: CategoryEntity)

    /*Transaction*/
    @Transaction
    @Query("SELECT * FROM tasks")
    fun task(): List<TaskDetail>?

    @Transaction
    @Query("SELECT * FROM tasks WHERE taskId = :taskId")
    fun task(taskId: Long): TaskDetail?
}