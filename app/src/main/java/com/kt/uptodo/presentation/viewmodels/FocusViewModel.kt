package com.kt.uptodo.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.uptodo.data.UptodoDatabase
import com.kt.uptodo.extensions.toSeconds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class FocusViewModel @Inject constructor(
    private val database: UptodoDatabase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val duration = savedStateHandle.get<Long>("duration")!!

    var focusTime: LocalTime = if (duration != 0L) {
        LocalTime.ofSecondOfDay(duration)
    } else {
        LocalTime.of(0, 0, 0)
    }

    init {
        Timber.d(duration.toString())
        Timber.d(focusTime.toString())
    }

    val remainingTime = MutableStateFlow<LocalTime>(LocalTime.of(0, 0, 0))
    val focusProgress = MutableStateFlow(1f)
    val focusState = MutableStateFlow(FocusState.STOPPED)

    val focusHistory = database.focusSessions()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private var job: Job? = null

    fun onAction(action: FocusUiAction) {
        when (action) {
            is FocusUiAction.SetFocusTime -> {
                focusTime = action.time
            }

            FocusUiAction.EndFocus -> endFocus()

            FocusUiAction.StartFocus -> {
                startFocus()
            }

            FocusUiAction.ToggleFocus -> toggleFocus()
        }
    }

    private fun toggleFocus() {
        viewModelScope.launch {
            Timber.d(focusState.value.toString())
            if (focusState.value == FocusState.RUNNING) {
                job?.cancel()
                job = null
                focusState.value = FocusState.PAUSED
            } else if (focusState.value == FocusState.PAUSED) {
                focusState.value = FocusState.RUNNING
                job = loadProgress {
                    focusProgress.value = it
                }
            }
        }
    }


    //save focus time into database
    private fun endFocus() {
        viewModelScope.launch(Dispatchers.IO) {
            focusState.value = FocusState.STOPPED
            job?.cancel()
            job = null
            remainingTime.value = LocalTime.of(0, 0, 0)
            focusProgress.value = 1f
        }
    }

    private fun startFocus() {
        remainingTime.value = focusTime
        job = loadProgress {
            focusProgress.value = it
        }
    }

    private fun reset() {
        viewModelScope.launch {
            focusState.value = FocusState.STOPPED
            focusProgress.value = 0f
        }
    }


    private fun loadProgress(updateProgress: (Float) -> Unit): Job {
        focusState.value = FocusState.RUNNING
        return viewModelScope.launch {
            val totalTime = focusTime.toSeconds()
            val countdownJob = launch {
                for (i in remainingTime.value.toSeconds() downTo 1) {
                    delay(1000)
                    remainingTime.value = remainingTime.value.minusSeconds(1)
                    updateProgress((remainingTime.value.toSeconds().toFloat() / totalTime))
                }
            }
            joinAll(countdownJob)

            focusState.value = FocusState.STOPPED
        }
    }
}


sealed interface FocusUiAction {
    data class SetFocusTime(val time: LocalTime) : FocusUiAction
    data object StartFocus : FocusUiAction
    data object ToggleFocus : FocusUiAction
    data object EndFocus : FocusUiAction
}

enum class FocusState {
    RUNNING, PAUSED, STOPPED;
}