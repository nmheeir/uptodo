package com.kt.uptodo.presentation.components.calendar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.util.fastForEach
import com.kt.uptodo.extensions.displayText
import java.time.DayOfWeek


@Composable
fun MonthHeader(
    modifier: Modifier = Modifier,
    daysOfWeek: List<DayOfWeek>
) {
    Row(modifier = modifier.fillMaxWidth()) {
        daysOfWeek.fastForEach { dayOfWeek ->
            Text(
                text = dayOfWeek.displayText(),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }
}