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
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseDao {

    /*Insert*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryEntity)

    /*Update*/
    @Update
    suspend fun update(task: TaskEntity)

    @Update
    suspend fun update(category: CategoryEntity)

    /*Delete*/
    @Delete
    suspend fun delete(task: TaskEntity)

    @Delete
    suspend fun delete(category: CategoryEntity)

    /*Query*/
    @Query("SELECT * FROM categories")
    fun categories(): Flow<List<CategoryEntity>>

    /*Transaction*/
    @Transaction
    @Query("SELECT * FROM tasks")
    fun task(): Flow<List<TaskDetail>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE taskId = :taskId")
    suspend fun task(taskId: Long): TaskDetail?
}