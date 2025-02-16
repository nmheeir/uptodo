package com.kt.uptodo.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kt.uptodo.data.relations.TaskDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val taskId: Long = savedStateHandle.get<Long>("taskId")!!

    val taskDetail = MutableStateFlow<TaskDetail?>(null)

    init {
        Timber.d(taskId.toString())
    }
}

/*

sealed interface TaskDetailAction {
    data object
}*/
