package com.kt.uptodo.presentation.components.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kt.uptodo.core.presentation.TimeFormat
import com.kt.uptodo.presentation.components.wheel.WheelTimePicker
import java.time.LocalTime

@Composable
fun WheelTimePickerDialog(
    taskTime: LocalTime,
    onTaskTimeChange: (LocalTime) -> Unit
) {
    var time by remember { mutableStateOf(taskTime) }
    WheelTimePicker(
        modifier = Modifier.fillMaxWidth(),
        startTime = time,
        timeFormat = TimeFormat.HOUR_24,
        onSnappedTime = { newTime ->
            time = newTime
            onTaskTimeChange(newTime)
        }
    )
}