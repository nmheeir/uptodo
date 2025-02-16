package com.kt.uptodo.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.uptodo.data.UptodoDatabase
import com.kt.uptodo.data.relations.TaskDetail
import com.kt.uptodo.utils.fakeTaskDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IndexViewModel @Inject constructor(
    private val database: UptodoDatabase
) : ViewModel() {

    val isLoading = MutableStateFlow(false)

    val taskDetails = MutableStateFlow<List<TaskDetail>>(emptyList())

    init {
        isLoading.value = true
        viewModelScope.launch {
            getTasks()
            isLoading.value = false
        }
    }

    private suspend fun getTasks() {
//        delay(1000)
        taskDetails.value = fakeTaskDetails
    }
}

