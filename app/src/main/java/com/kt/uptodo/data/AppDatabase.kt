package com.kt.uptodo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kt.uptodo.data.entities.CategoryEntity
import com.kt.uptodo.data.entities.FocusSessionEntity
import com.kt.uptodo.data.entities.TaskEntity


class UptodoDatabase(
    private val delegate: InternalDatabase
) : DatabaseDao by delegate.dao {

    fun query(block: UptodoDatabase.() -> Unit) = with(delegate) {
        queryExecutor.execute {
            block(this@UptodoDatabase)
        }
    }

    fun transaction(block: UptodoDatabase.() -> Unit) = with(delegate) {
        transactionExecutor.execute {
            runInTransaction {
                block(this@UptodoDatabase)
            }
        }
    }

    fun close() = delegate.close()

}


@Database(
    entities = [
        TaskEntity::class,
        CategoryEntity::class,
        FocusSessionEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class InternalDatabase : RoomDatabase() {

    abstract val dao: DatabaseDao

    companion object {
        private const val DB_NAME = "uptodo.db"

        fun newInstance(context: Context) =
            UptodoDatabase(
                delegate = Room.databaseBuilder(context, InternalDatabase::class.java, DB_NAME)
//                    .addCallback(roomCallback)
                    .fallbackToDestructiveMigration()
                    .build()
            )
    }
}