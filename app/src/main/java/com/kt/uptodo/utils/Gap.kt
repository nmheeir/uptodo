package com.kt.uptodo.utils

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun RowScope.Gap(modifier: Modifier = Modifier, width: Dp) {
    Spacer(modifier = modifier.width(width))
}

@Composable
fun ColumnScope.Gap(modifier: Modifier = Modifier, height: Dp) {
    Spacer(modifier = modifier.height(height))
}