package com.kt.uptodo.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.uptodo.data.UptodoDatabase
import com.kt.uptodo.data.enums.Priority
import com.kt.uptodo.presentation.shared.NewTaskSheetUiAction
import com.kt.uptodo.utils.DefaultTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class IndexViewModel @Inject constructor(
    private val database: UptodoDatabase
) : ViewModel() {

    val allTasks = database.task()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}