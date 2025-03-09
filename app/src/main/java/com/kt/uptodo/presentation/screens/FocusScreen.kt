package com.kt.uptodo.presentation.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.uptodo.R
import com.kt.uptodo.extensions.parse
import com.kt.uptodo.presentation.LocalWindowInsets
import com.kt.uptodo.presentation.components.wheel.WheelTimePicker
import com.kt.uptodo.presentation.viewmodels.FocusState
import com.kt.uptodo.presentation.viewmodels.FocusUiAction
import com.kt.uptodo.presentation.viewmodels.FocusViewModel
import com.kt.uptodo.utils.Gap
import com.kt.uptodo.utils.padding
import timber.log.Timber
import java.time.LocalTime

@Composable
fun FocusScreen(
    navController: NavHostController,
    viewModel: FocusViewModel = hiltViewModel()
) {
    CompositionLocalProvider(
        LocalViewModel provides viewModel
    ) {
        Scaffold(
            topBar = {
                Text(
                    text = "Focus Mode",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            modifier = Modifier.padding(LocalWindowInsets.current.asPaddingValues())
        ) { contentPadding ->
            val focusHistory by viewModel.focusHistory.collectAsStateWithLifecycle()
            LazyColumn(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                item {
                    FocusSection(
                        viewModel = viewModel
                    )
                }

                //Focus history
                items(
                    items = focusHistory,
                    key = { it.focusSessionId }
                ) { focusSession ->
                    Text(
                        text = focusSession.duration.toString()
                    )
                    Gap(MaterialTheme.padding.medium)
                }
            }
        }
    }
}

@Composable
private fun FocusSection(
    modifier: Modifier = Modifier,
    viewModel: FocusViewModel
) {
//    val viewModel = LocalViewModel.current
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        val focusState by viewModel.focusState.collectAsStateWithLifecycle()
        AnimatedContent(
            targetState = focusState,
            contentKey = {
                when (it) {
                    FocusState.STOPPED -> "change"
                    else -> "nothing"
                }
            }
        ) { state ->
            if (state == FocusState.STOPPED) {
                SelectFocusTime(
                    action = viewModel::onAction,
                    startTime = viewModel.focusTime
                )
            } else {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    val focusProgress by viewModel.focusProgress.collectAsStateWithLifecycle()
                    CircularProgressIndicator(
                        progress = { focusProgress },
                        color = MaterialTheme.colorScheme.tertiary,
                        strokeWidth = 10.dp,
                        modifier = Modifier
                            .size(256.dp),
                    )
                    val focusTime by viewModel.remainingTime.collectAsStateWithLifecycle()
                    Text(
                        text = focusTime.parse(),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        FocusSectionButton(focusState = focusState, action = viewModel::onAction)
    }
}

@Composable
private fun FocusSectionButton(
    modifier: Modifier = Modifier,
    focusState: FocusState,
    action: (FocusUiAction) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = {
                if (focusState == FocusState.STOPPED) {
                    action(FocusUiAction.StartFocus)
                } else {
                    action(FocusUiAction.ToggleFocus)
                }
            }
        ) {
            Icon(
                painter = painterResource(
                    if (focusState == FocusState.RUNNING) R.drawable.ic_pause
                    else R.drawable.ic_play_arrow
                ),
                contentDescription = null
            )
        }

        if (focusState != FocusState.STOPPED) {
            IconButton(
                onClick = {
                    action(FocusUiAction.EndFocus)
                    Timber.d("End focus")
                }
            ) {
                Icon(painterResource(R.drawable.ic_refresh), null)
            }
        }
    }
}

@Composable
private fun SelectFocusTime(
    modifier: Modifier = Modifier,
    startTime: LocalTime,
    action: (FocusUiAction) -> Unit
) {
    WheelTimePicker(
        startTime = startTime,
        size = DpSize(128.dp, 128.dp),
        modifier = modifier
    ) { time ->
        action(FocusUiAction.SetFocusTime(time))
    }
}

private val LocalViewModel = compositionLocalOf<FocusViewModel> { error("No ViewModel provided") }
