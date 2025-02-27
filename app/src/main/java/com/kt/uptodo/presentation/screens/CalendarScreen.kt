package com.kt.uptodo.presentation.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.WeekDayPosition
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import com.kt.uptodo.presentation.LocalWindowInsets
import com.kt.uptodo.presentation.components.TaskItem
import com.kt.uptodo.presentation.components.calendar.CalendarHeader
import com.kt.uptodo.presentation.components.calendar.CalendarTitle
import com.kt.uptodo.presentation.components.calendar.Day
import com.kt.uptodo.presentation.theme.UpTodoTheme
import com.kt.uptodo.presentation.viewmodels.CalendarUiAction
import com.kt.uptodo.presentation.viewmodels.CalendarViewModel
import com.kt.uptodo.utils.padding
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
            val selectedDay by viewModel.selectedDay.collectAsStateWithLifecycle()
            CalendarTopBar(
                selectedDay = selectedDay,
                action = viewModel::onAction
            )
        },
        modifier = Modifier
            .padding(LocalWindowInsets.current.asPaddingValues())
    ) { contentPadding ->

        val todayTasks by viewModel.todayTasks.collectAsStateWithLifecycle()
        val completeTasks by remember {
            derivedStateOf {
                todayTasks.filter { it.task.isComplete }
            }
        }

        Box(
            modifier = Modifier.padding(contentPadding)
        ) {
            var tab by remember { mutableIntStateOf(0) }
            LazyColumn {
                item(
                    key = "calendar_pager"
                ) {
                    CalendarPager(
                        onClick = { tab = it }
                    )
                }

                items(
                    items = if (tab == 0) todayTasks else completeTasks,
                    key = { it.hashCode() }
                ) {
                    TaskItem(
                        taskDetail = it,
                        onClick = {
                            navController.navigate("task_detail/${it.task.taskId}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarTopBar(
    selectedDay: LocalDate,
    action: (CalendarUiAction) -> Unit
) {
    val currentDate = remember { LocalDate.now() }
    val currentMonth = remember(currentDate) { currentDate.yearMonth }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val daysOfWeek = remember { daysOfWeek() }
    var selection: LocalDate by remember { mutableStateOf(selectedDay) }

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
                        if (selection != it) {
                            selection = it
                            action(CalendarUiAction.UpdateSelectedDay(it))
                        }
                    }
                )
            }
        )
    }
}

@Composable
private fun CalendarPager(
    onClick: (Int) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextButton(
            onClick = {
                onClick(0)
                selectedTab = 0
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.textButtonColors(
                containerColor = Color.Transparent,
                contentColor = if (selectedTab == 1) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text("Today")
        }

        TextButton(
            onClick = {
                onClick(1)
                selectedTab = 1
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.textButtonColors(
                containerColor = Color.Transparent,
                contentColor = if (selectedTab == 2) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text("Completed")
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun Test() {
    UpTodoTheme {
        CalendarPager { }
    }
}