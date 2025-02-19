package com.kt.uptodo.presentation.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kt.uptodo.extensions.displayText
import com.kt.uptodo.presentation.components.CalendarTitle
import com.kt.uptodo.utils.clickable
import com.kt.uptodo.utils.rememberFirstMostVisibleMonth
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarDialog(
    taskDate: LocalDate,
    onTaskDateChange: (LocalDate) -> Unit
) {
    val currentMonth = remember { YearMonth.of(taskDate.year, taskDate.month) }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val daysOfWeek = remember { daysOfWeek() }
    var selection by remember {
        mutableStateOf<CalendarDay>(
            CalendarDay(
                date = taskDate,
                position = DayPosition.MonthDate
            )
        )
    }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )
    val coroutineScope = rememberCoroutineScope()
    val visibleMonth = rememberFirstMostVisibleMonth(state, viewportPercent = 90f)

    CalendarTitle(
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
        currentMonth = visibleMonth.yearMonth,
        goToPrevious = {
            coroutineScope.launch {
                state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
            }
        },
        goToNext = {
            coroutineScope.launch {
                state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
            }
        },
    )
    HorizontalCalendar(
        modifier = Modifier,
        state = state,
        dayContent = { day ->
            Day(
                day = day,
                isSelected = selection == day
            ) { clicked ->
                selection = clicked
                onTaskDateChange(clicked.date)
            }
        },
        monthHeader = {
            MonthHeader(daysOfWeek = daysOfWeek)
        },
    )
}

@Composable
private fun MonthHeader(
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

@Composable
private fun Day(day: CalendarDay, isSelected: Boolean, onClick: (CalendarDay) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(6.dp)
            .clip(CircleShape)
            .background(color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                showRipple = !isSelected,
                onClick = { onClick(day) },
            ),
        contentAlignment = Alignment.Center,
    ) {
        val textColor = when (day.position) {
            // Color.Unspecified will use the default text color from the current theme
            DayPosition.MonthDate -> if (isSelected) MaterialTheme.colorScheme.onPrimary else Color.Unspecified
            DayPosition.InDate, DayPosition.OutDate -> MaterialTheme.colorScheme.outlineVariant
        }
        Text(
            text = day.date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 14.sp,
        )
    }
}