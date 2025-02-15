package com.kt.uptodo.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.uptodo.data.UptodoDatabase
import com.kt.uptodo.data.relations.TaskDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IndexViewModel @Inject constructor(
    private val database: UptodoDatabase
) : ViewModel() {

    val tasks = MutableStateFlow<List<TaskDetail>>(emptyList())

    init {
        viewModelScope.launch {
            getTasks()
        }
    }

    private suspend fun getTasks() {
//        tasks.value = database.query { database.task() }
    }
}

