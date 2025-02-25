package com.kt.uptodo.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kt.uptodo.data.entities.CategoryEntity
import com.kt.uptodo.extensions.toHex

object Constants {
    val NavigationBarHeight = 80.dp
}


val DefaultCategoryColors = listOf(
    Color(0xFFC9CC41),
    Color(0xFF66CC41),
    Color(0xFF41CCA7),
    Color(0xFF4181CC),
    Color(0xFF41A2CC),
    Color(0xFFCC4141),
    Color(0xFFCC8441),
    Color(0xFF9741CC),
    Color(0xFFCC4173),
    Color(0xFFCC41A2)
)

val DefaultCategory = CategoryEntity(
    categoryId = 0,
    name = "",
    color = DefaultCategoryColors[0].toHex()
)
