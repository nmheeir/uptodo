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
    }
}