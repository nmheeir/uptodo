package com.kt.uptodo.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

const val SECONDARY_ALPHA = 0.78f
fun Modifier.secondaryItemAlpha(): Modifier = this.alpha(SECONDARY_ALPHA)
