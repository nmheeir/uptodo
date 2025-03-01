package com.kt.uptodo.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.uptodo.data.UptodoDatabase
import com.kt.uptodo.extensions.toSeconds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class FocusViewModel @Inject constructor(
    private val database: UptodoDatabase
) : ViewModel() {

    val focusTime = MutableStateFlow(LocalTime.of(0, 2, 0))
    val focusProgress = MutableStateFlow(0f)
    val focusState = MutableStateFlow(FocusState.END)

    fun onAction(action: FocusUiAction) {
        when (action) {
            is FocusUiAction.SetFocusTime -> {
                focusTime.value = action.time
            }

            is FocusUiAction.UpdateFocusTime -> {}
            is FocusUiAction.UpdateFocusProgress -> {}
            FocusUiAction.StartFocusSession -> {
                startFocus()
            }

            FocusUiAction.EndFocusSession -> {
                endFocus()
            }

            FocusUiAction.PauseFocusSession -> {
                pauseFocus()
            }
        }
    }

    private fun pauseFocus() {
        TODO("Not yet implemented")
    }

    private fun endFocus() {
        //Save focus to db
    }

    private fun startFocus() {
        loadProgress {
            focusProgress.value = it
        }
    }

    private fun updateFocusTime(time: Long) {

    }

    private fun setFocusTime(time: LocalTime) {
        viewModelScope.launch {
            focusTime.value = time
        }
    }


    private fun loadProgress(updateProgress: (Float) -> Unit) {
        focusState.value = FocusState.RUNNING
        viewModelScope.launch {
            val totalTime = focusTime.value.toSeconds()

            val countdownJob = launch {
                for (i in focusTime.value.toSeconds() downTo 1) {
                    delay(1000)
                    focusTime.value = focusTime.value.minusSeconds(1)
                }
            }

            val progressJob = launch {
                for (i in totalTime downTo 1) {
                    updateProgress(i.toFloat() / totalTime)
                    Timber.d(i.toString())
                    delay(1000)
                }
            }

            joinAll(countdownJob, progressJob)

            focusState.value = FocusState.END
        }
    }


}


sealed interface FocusUiAction {
    data class SetFocusTime(val time: LocalTime) : FocusUiAction
    data class UpdateFocusTime(val time: LocalTime) : FocusUiAction
    data class UpdateFocusProgress(val progress: Float) : FocusUiAction
    data object StartFocusSession : FocusUiAction
    data object PauseFocusSession : FocusUiAction
    data object EndFocusSession : FocusUiAction
}

enum class FocusState {
    RUNNING, PAUSE, END;
}