package com.kt.uptodo.presentation.screens

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.uptodo.extensions.parse
import com.kt.uptodo.presentation.LocalWindowInsets
import com.kt.uptodo.presentation.viewmodels.FocusState
import com.kt.uptodo.presentation.viewmodels.FocusUiAction
import com.kt.uptodo.presentation.viewmodels.FocusViewModel

@Composable
fun FocusScreen(
    navController: NavHostController,
    viewModel: FocusViewModel = hiltViewModel()
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
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            item {
                val focusTime by viewModel.focusTime.collectAsStateWithLifecycle()
                val focusProgress by viewModel.focusProgress.collectAsStateWithLifecycle()
                val focusState by viewModel.focusState.collectAsStateWithLifecycle()

                val animProgress by animateFloatAsState(
                    targetValue = focusProgress,
                    label = "focusProgress",
                    animationSpec = tween(
                        durationMillis = 100,
                        easing = LinearOutSlowInEasing
                    )
                )

                val parseFocusTime by remember {
                    derivedStateOf { focusTime.parse() }
                }

                FocusSection(
                    focusTime = { parseFocusTime },
                    focusProgress = { animProgress },
                    focusState = focusState,
                    action = viewModel::onAction
                )
            }
        }
    }
}

@Composable
private fun FocusSection(
    modifier: Modifier = Modifier,
    focusTime: () -> String,
    focusProgress: () -> Float,
    focusState: FocusState,
    action: (FocusUiAction) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        if (focusState == FocusState.RUNNING) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = focusProgress,
                    color = MaterialTheme.colorScheme.tertiary,
                    strokeWidth = 10.dp,
                    modifier = Modifier
                        .size(256.dp),
                )
                Text(
                    text = focusTime(),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Button(
            onClick = {
                action(FocusUiAction.StartFocusSession)
            },
            enabled = focusState == FocusState.END
        ) {
            Text(
                text = "Focus"
            )
        }
    }
}
