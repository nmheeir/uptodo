package com.kt.uptodo.presentation.components.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.commandiron.wheel_picker_compose.WheelTimePicker
import com.commandiron.wheel_picker_compose.core.SelectorProperties
import com.commandiron.wheel_picker_compose.core.TimeFormat
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import java.time.LocalDateTime
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