package com.kt.uptodo.extensions

import androidx.compose.ui.graphics.Color

fun Color.Companion.parseColor(colorString: String): Color {
    return Color(android.graphics.Color.parseColor(colorString))
}