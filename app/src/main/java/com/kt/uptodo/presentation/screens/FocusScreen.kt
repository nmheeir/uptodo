package com.kt.uptodo.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kt.uptodo.core.presentation.DefaultWheelTimePicker
import com.kt.uptodo.core.presentation.TimeFormat
import com.kt.uptodo.core.presentation.WheelPickerDefaults
import com.kt.uptodo.presentation.LocalWindowInsets
import com.kt.uptodo.presentation.components.wheel.WheelTimePicker
import com.kt.uptodo.presentation.viewmodels.FocusViewModel
import com.kt.uptodo.utils.padding
import timber.log.Timber

@Composable
fun FocusScreen(
    navController: NavHostController,
    focusViewModel: FocusViewModel = hiltViewModel()
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
        ) {
            item {
                FocusSection()
            }
        }
    }
}

@Composable
private fun FocusSection(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.padding.mediumSmall)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            WheelTimePicker(
                size = DpSize(256.dp, 256.dp)
            ) {
                Timber.d(it.toString())
            }
        }

        Text(
            text = "While your focus mode is on, all of your notifications will be off",
            textAlign = TextAlign.Center
        )

        Button(
            onClick = {}
        ) {
            Text(
                text = "Start Focusing"
            )
        }
    }

}

@Composable
private fun OverviewSection(modifier: Modifier = Modifier) {

}