package com.kt.uptodo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.kt.uptodo.data.entities.C_CATEGORY_ID
import com.kt.uptodo.data.entities.C_CREATED_AT
import com.kt.uptodo.data.entities.C_DEADLINE
import com.kt.uptodo.data.entities.C_IS_COMPLETE
import com.kt.uptodo.data.entities.C_IS_REMINDER
import com.kt.uptodo.data.entities.C_PARENT_TASK
import com.kt.uptodo.data.entities.C_PRIORITY
import com.kt.uptodo.data.entities.C_TASK_ID
import com.kt.uptodo.data.entities.C_UPDATED_AT
import com.kt.uptodo.data.entities.CategoryEntity
import com.kt.uptodo.data.entities.TaskEntity
import com.kt.uptodo.data.enums.Priority
import com.kt.uptodo.data.relations.TaskDetail
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

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
    @Query("SELECT * FROM tasks WHERE $C_TASK_ID = :taskId")
    fun task(taskId: Long): Flow<TaskDetail?>

    @Transaction
    @Query("SELECT * FROM tasks")
    fun tasks(): Flow<List<TaskDetail>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE $C_CREATED_AT = :createAt")
    fun tasks(createAt: LocalDateTime): Flow<List<TaskDetail>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE $C_IS_COMPLETE = :isComplete")
    fun tasks(isComplete: Boolean): Flow<List<TaskDetail>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE $C_IS_COMPLETE = :isComplete AND $C_CREATED_AT = :createAt")
    fun tasks(isComplete: Boolean, createAt: LocalDateTime): Flow<List<TaskDetail>>

    @Transaction
    @Query(
        """
            SELECT * FROM $TABLE_TASK
            WHERE (:taskId IS NULL OR $C_TASK_ID = :taskId)
                AND (:parentTask IS NULL OR $C_PARENT_TASK = :parentTask)
                AND (:categoryId IS NULL OR $C_CATEGORY_ID = :categoryId)
                AND (:priority IS NULL OR $C_PRIORITY = :priority)
                AND (:isComplete IS NULL OR $C_IS_COMPLETE = :isComplete)
                AND (:isReminder IS NULL OR $C_IS_REMINDER = :isReminder)
                AND (:createdAt IS NULL OR DATE($C_CREATED_AT) = DATE(:createdAt))
                AND (:updatedAt IS NULL OR DATE($C_UPDATED_AT) = DATE(:updatedAt))
                AND (:deadline IS NULL OR DATE($C_DEADLINE) = DATE(:deadline))
        """
    )
    fun getTasks(
        taskId: Long? = null,
        parentTask: Long? = null,
        categoryId: Long? = null,
        priority: Priority? = null,
        isComplete: Boolean? = null,
        isReminder: Boolean? = null,
        createdAt: LocalDateTime? = null,
        updatedAt: LocalDateTime? = null,
        deadline: LocalDateTime? = null
    ): Flow<List<TaskDetail>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE $C_PARENT_TASK = :parentTaskId")
    fun subTasks(parentTaskId: Long): Flow<List<TaskDetail>>
}