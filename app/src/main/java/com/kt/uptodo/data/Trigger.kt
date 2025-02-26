package com.kt.uptodo.data

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

val roomCallback = object : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        // Trigger 1: Tự động cập nhật updated_at khi có chỉnh sửa
        db.execSQL(
            """
            CREATE TRIGGER update_timestamp 
            AFTER UPDATE ON $TABLE_TASK
            FOR EACH ROW 
            BEGIN 
                UPDATE $TABLE_TASK SET updated_at = CURRENT_TIMESTAMP WHERE id = OLD.id; 
            END;
            """
        )

        //Đặt lại parentTask (của task con) = null  nếu parentTask bị xóa
/*        db.execSQL(
            """
                            CREATE TRIGGER IF NOT EXISTS update_child_tasks
                            AFTER DELETE ON $TABLE_TASK
                            FOR EACH ROW
                            BEGIN
                                UPDATE $TABLE_TASK
                                SET $C_PARENT_TASK = NULL
                                WHERE $C_PARENT_TASK = OLD.$C_TASK_ID;
                            END;
                            """.trimIndent()
        )*/
    }
}