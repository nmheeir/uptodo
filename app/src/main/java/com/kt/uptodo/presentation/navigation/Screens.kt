package com.kt.uptodo.presentation.navigation

sealed class Screens(val route: String) {
    data object Index : Screens("index")
    data object Calendar : Screens("calendar")
    data object Focus : Screens("focus")
    data object Category: Screens("category")
    data object Task: Screens("task")

    data object Profile : Screens("profile")
}