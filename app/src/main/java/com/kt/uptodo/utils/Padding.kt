package com.kt.uptodo.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp

class Padding {

    val extraLarge = 32.dp

    val large = 24.dp

    val mediumLarge = 28.dp

    val medium = 16.dp

    val mediumSmall = 12.dp

    val small = 8.dp

    val extraSmall = 4.dp
}

val MaterialTheme.padding: Padding
    get() = Padding()