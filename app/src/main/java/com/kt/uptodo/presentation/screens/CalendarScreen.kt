package com.kt.uptodo.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.WeekDayPosition
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.core.yearMonth
import com.kt.uptodo.presentation.LocalWindowInsets
import com.kt.uptodo.presentation.components.calendar.CalendarHeader
import com.kt.uptodo.presentation.components.calendar.CalendarTitle
import com.kt.uptodo.presentation.components.calendar.Day
import com.kt.uptodo.presentation.viewmodels.CalendarViewModel
import com.kt.uptodo.utils.rememberFirstMostVisibleMonth
import com.kt.uptodo.utils.rememberFirstVisibleWeekAfterScroll
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun CalendarScreen(
    navController: NavHostController,
    viewModel: CalendarViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            CalendarTopBar()
        },
        modifier = Modifier
            .padding(LocalWindowInsets.current.asPaddingValues())
    ) { contentPadding ->
        Box(
            modifier = Modifier.padding(contentPadding)
        ) {

        }
    }
}

@Composable
private fun CalendarTopBar() {
    val currentDate = remember { LocalDate.now() }
    val currentMonth = remember(currentDate) { currentDate.yearMonth }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val daysOfWeek = remember { daysOfWeek() }
    var selection: LocalDate by remember { mutableStateOf(LocalDate.now()) }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )
    val weekState = rememberWeekCalendarState(
        startDate = startMonth.atStartOfMonth(),
        endDate = endMonth.atEndOfMonth(),
        firstVisibleWeekDate = currentDate,
        firstDayOfWeek = daysOfWeek.first(),
    )

    val coroutineScope = rememberCoroutineScope()
    val visibleWeek = rememberFirstVisibleWeekAfterScroll(weekState)

    Column {
        CalendarTitle(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            currentMonth = visibleWeek.days.first().date.yearMonth,
            goToPrevious = {
                coroutineScope.launch {
                    val targetDate = weekState.firstVisibleWeek.days.first().date.minusDays(1)
                    weekState.animateScrollToWeek(targetDate)
                }
            },
            goToNext = {
                coroutineScope.launch {
                    val targetDate = weekState.firstVisibleWeek.days.last().date.plusDays(1)
                    weekState.animateScrollToWeek(targetDate)
                }
            },
        )

        CalendarHeader(daysOfWeek)

        WeekCalendar(
            state = weekState,
            dayContent = { day ->
                val isSelectable = day.position == WeekDayPosition.RangeDate
                Day(
                    day = day.date,
                    isSelected = selection == day.date,
                    isSelectable = isSelectable,
                    onClick = {
                        selection = it
                    }
                )
            }
        )
    }
}