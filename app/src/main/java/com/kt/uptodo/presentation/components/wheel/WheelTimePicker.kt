package com.kt.uptodo.presentation.components.wheel

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.kt.uptodo.core.presentation.DefaultWheelTimePicker
import com.kt.uptodo.core.presentation.SelectorProperties
import com.kt.uptodo.core.presentation.TimeFormat
import com.kt.uptodo.core.presentation.WheelPickerDefaults
import com.kt.uptodo.presentation.theme.UpTodoTheme
import java.time.LocalTime

@Composable
fun WheelTimePicker(
    modifier: Modifier = Modifier,
    startTime: LocalTime = LocalTime.now(),
    minTime: LocalTime = LocalTime.MIN,
    maxTime: LocalTime = LocalTime.MAX,
    timeFormat: TimeFormat = TimeFormat.HOUR_24,
    size: DpSize = DpSize(128.dp, 128.dp),
    rowCount: Int = 3,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    textColor: Color = LocalContentColor.current,
    selectorProperties: SelectorProperties = WheelPickerDefaults.selectorProperties(),
    onSnappedTime: (snappedTime: LocalTime) -> Unit = {},
) {
    DefaultWheelTimePicker(
        modifier,
        startTime,
        minTime,
        maxTime,
        timeFormat,
        size,
        rowCount,
        textStyle,
        textColor,
        selectorProperties,
        onSnappedTime = { snappedTime, _ ->
            onSnappedTime(snappedTime.snappedLocalTime)
            snappedTime.snappedIndex
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun Test() {
    UpTodoTheme {
        WheelTimePicker(
            size = DpSize(256.dp, 256.dp),
            selectorProperties = WheelPickerDefaults.selectorProperties(
                color = Color.Transparent
            ),
            timeFormat = TimeFormat.AM_PM
        )
    }
}