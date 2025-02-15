package com.kt.uptodo.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.kt.uptodo.presentation.screens.CalendarScreen
import com.kt.uptodo.presentation.screens.FocusScreen
import com.kt.uptodo.presentation.screens.IndexScreen
import com.kt.uptodo.presentation.screens.MoreScreen

fun NavGraphBuilder.navigationBuilder(
    navController: NavHostController
) {
    composable(route = Screens.Index.route) {
        IndexScreen(navController)
    }

    composable(
        route = Screens.Calendar.route
    ) {
        CalendarScreen()
    }

    composable(
        route = Screens.Focus.route
    ) {
        FocusScreen()
    }

    composable(
        route = Screens.More.route
    ) {
        MoreScreen()
    }
}