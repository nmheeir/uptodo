package com.kt.uptodo.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.uptodo.data.UptodoDatabase
import com.kt.uptodo.data.relations.TaskDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val database: UptodoDatabase
) : ViewModel() {

    val selectedDay = MutableStateFlow(LocalDate.now())

    @OptIn(ExperimentalCoroutinesApi::class)
    val todayTasks: StateFlow<List<TaskDetail>> = selectedDay.flatMapLatest { day ->
        database.getTasks(deadline = day.atStartOfDay())
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val unCompleteTasks: StateFlow<List<TaskDetail>> = todayTasks
        .map { tasks -> tasks.filter { !it.task.isComplete } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val completeTasks: StateFlow<List<TaskDetail>> = todayTasks
        .map { tasks -> tasks.filter { it.task.isComplete } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun onAction(action: CalendarUiAction) {
        when (action) {
            is CalendarUiAction.UpdateSelectedDay -> {
                updateSelectDay(action.newSelectDay)
            }
        }
    }

    private fun updateSelectDay(newSelectedDay: LocalDate) {
        selectedDay.value = newSelectedDay
        Timber.d("updateSelectDay: $newSelectedDay")
        Timber.d("todayTask: ${todayTasks.value}")
    }
}

sealed interface CalendarUiAction {
    data class UpdateSelectedDay(val newSelectDay: LocalDate) : CalendarUiAction
}