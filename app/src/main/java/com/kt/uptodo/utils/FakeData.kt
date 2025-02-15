package com.kt.uptodo.utils

import com.kt.uptodo.data.entities.TaskEntity
import com.kt.uptodo.data.enums.Priority
import java.time.OffsetDateTime

val fakeTasks = listOf(
    TaskEntity(
        taskId = 1,
        parentTask = null,
        categoryId = 1,
        title = "Mua sắm",
        description = "Mua rau và trái cây",
        priority = Priority.HIGH,
        createdAt = OffsetDateTime.now(),
        isComplete = false,
        isReminder = true,
        start = OffsetDateTime.now(),
        end = OffsetDateTime.now().plusHours(2)
    ),
    TaskEntity(
        taskId = 2,
        parentTask = null,
        categoryId = 2,
        title = "Làm bài tập",
        description = "Hoàn thành bài tập toán",
        priority = Priority.MEDIUM,
        createdAt = OffsetDateTime.now(),
        isComplete = false,
        isReminder = false,
        start = OffsetDateTime.now(),
        end = OffsetDateTime.now().plusHours(3)
    ),
    TaskEntity(
        taskId = 3,
        parentTask = null,
        categoryId = 1,
        title = "Dọn dẹp nhà",
        description = "Lau nhà và quét dọn",
        priority = Priority.LOW,
        createdAt = OffsetDateTime.now(),
        isComplete = true,
        isReminder = true,
        start = OffsetDateTime.now(),
        end = OffsetDateTime.now().plusHours(1)
    ),
    TaskEntity(
        taskId = 4,
        parentTask = 1,
        categoryId = 3,
        title = "Chuẩn bị họp",
        description = "Chuẩn bị tài liệu cho cuộc họp",
        priority = Priority.HIGH,
        createdAt = OffsetDateTime.now(),
        isComplete = false,
        isReminder = true,
        start = OffsetDateTime.now(),
        end = OffsetDateTime.now().plusHours(4)
    ),
    TaskEntity(
        taskId = 5,
        parentTask = null,
        categoryId = 4,
        title = "Tập thể dục",
        description = "Chạy bộ 30 phút",
        priority = Priority.MEDIUM,
        createdAt = OffsetDateTime.now(),
        isComplete = false,
        isReminder = false,
        start = OffsetDateTime.now(),
        end = OffsetDateTime.now().plusMinutes(30)
    ),
    TaskEntity(
        taskId = 6,
        parentTask = 3,
        categoryId = 2,
        title = "Viết báo cáo",
        description = "Hoàn thành báo cáo tuần",
        priority = Priority.HIGH,
        createdAt = OffsetDateTime.now(),
        isComplete = false,
        isReminder = true,
        start = OffsetDateTime.now(),
        end = OffsetDateTime.now().plusHours(5)
    ),
    TaskEntity(
        taskId = 7,
        parentTask = null,
        categoryId = 5,
        title = "Đọc sách",
        description = "Đọc 10 trang sách",
        priority = Priority.LOW,
        createdAt = OffsetDateTime.now(),
        isComplete = false,
        isReminder = false,
        start = OffsetDateTime.now(),
        end = OffsetDateTime.now().plusHours(1)
    ),
    TaskEntity(
        taskId = 8,
        parentTask = 2,
        categoryId = 3,
        title = "Nấu ăn",
        description = "Chuẩn bị bữa tối",
        priority = Priority.MEDIUM,
        createdAt = OffsetDateTime.now(),
        isComplete = true,
        isReminder = false,
        start = OffsetDateTime.now(),
        end = OffsetDateTime.now().plusHours(2)
    ),
    TaskEntity(
        taskId = 9,
        parentTask = 5,
        categoryId = 4,
        title = "Đi siêu thị",
        description = "Mua nguyên liệu cho bữa tối",
        priority = Priority.HIGH,
        createdAt = OffsetDateTime.now(),
        isComplete = false,
        isReminder = true,
        start = OffsetDateTime.now(),
        end = OffsetDateTime.now().plusHours(2)
    ),
    TaskEntity(
        taskId = 10,
        parentTask = null,
        categoryId = 1,
        title = "Học lập trình",
        description = "Học Kotlin trong 1 giờ",
        priority = Priority.HIGH,
        createdAt = OffsetDateTime.now(),
        isComplete = false,
        isReminder = true,
        start = OffsetDateTime.now(),
        end = OffsetDateTime.now().plusHours(1)
    )
)
