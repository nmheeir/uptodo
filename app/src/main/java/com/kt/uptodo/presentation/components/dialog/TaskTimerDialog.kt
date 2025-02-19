package com.kt.uptodo.presentation.components.dialog

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.kt.uptodo.R
import java.time.LocalDateTime

@Composable
fun TaskTimeDialog(
    taskDateTime: LocalDateTime,
    onDismiss: () -> Unit,
    onTaskDateTimeChange: (LocalDateTime) -> Unit
) {
    var currentStep by remember { mutableIntStateOf(0) }

    val (dateTime, onDateTimeChange) = remember { mutableStateOf(taskDateTime) }

    DefaultDialog(
        onDismiss = {
            if (currentStep == 0) onDismiss() else currentStep--
        },
        buttons = {
            TextButton(
                onClick = {
                    if (currentStep == 0) onDismiss() else currentStep--
                }
            ) {
                Text(
                    text = stringResource(if (currentStep == 0) R.string.action_cancel else R.string.action_back)
                )
            }
            TextButton(
                onClick = {
                    if (currentStep == 0) currentStep++
                    else {
                        onTaskDateTimeChange(dateTime)
                        onDismiss()
                    }
                }
            ) {
                Text(text = stringResource(if (currentStep == 0) R.string.action_next else R.string.ok))
            }
        }
    ) {
        when (currentStep) {
            0 -> {
                CalendarDialog(
                    taskDate = dateTime.toLocalDate(),
                    onTaskDateChange = {
                        onDateTimeChange(dateTime.with(it))
                    }
                )
            }

            1 -> {
                WheelTimePickerDialog(
                    taskTime = dateTime.toLocalTime(),
                    onTaskTimeChange = {
                        onDateTimeChange(dateTime.with(it))
                    }
                )
            }
        }
    }
}