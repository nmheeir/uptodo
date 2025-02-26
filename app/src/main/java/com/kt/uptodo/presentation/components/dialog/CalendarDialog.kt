package com.kt.uptodo.presentation.components.dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kt.uptodo.presentation.components.calendar.CalendarTitle
import com.kt.uptodo.presentation.components.calendar.Day
import com.kt.uptodo.presentation.components.calendar.MonthHeader
import com.kt.uptodo.utils.rememberFirstMostVisibleMonth
import kotlinx.coroutines.launch
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
        mutableStateOf<LocalDate>(LocalDate.now())
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
            val isSelectable = day.position == DayPosition.MonthDate
            Day(
                day = day.date,
                isSelected = selection == day.date,
                isSelectable = isSelectable,
                onClick = {
                    selection = it
                    onTaskDateChange(it)
                }
            )
        },
        monthHeader = {
            MonthHeader(daysOfWeek = daysOfWeek)
        },
    )

}