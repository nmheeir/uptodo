package com.kt.uptodo.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.util.Locale

fun Color.toHex(): String {
    return String.format(Locale.getDefault(), "#%08X", this.toArgb())
}