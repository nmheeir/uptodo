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
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.WeekDayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.core.yearMonth
import com.kt.uptodo.presentation.LocalWindowInsets
import com.kt.uptodo.presentation.components.calendar.CalendarTitle
import com.kt.uptodo.presentation.components.calendar.Day
import com.kt.uptodo.presentation.viewmodels.CalendarViewModel
import com.kt.uptodo.utils.rememberFirstMostVisibleMonth
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
    var selection by remember {
        mutableStateOf(
            CalendarDay(
                date = currentDate,
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

    Column {
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

        WeekCalendar(
            dayContent = { day ->
                val isSelectable = day.position == WeekDayPosition.RangeDate
                Day(
                    day = day.date,
                    isSelected = selection.date == day.date,
                    isSelectable = isSelectable,
                    onClick = { }
                )
            }
        )
    }
}