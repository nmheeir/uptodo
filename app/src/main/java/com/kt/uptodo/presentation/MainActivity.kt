package com.kt.uptodo.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastForEach
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kt.uptodo.data.UptodoDatabase
import com.kt.uptodo.presentation.navigation.Screens
import com.kt.uptodo.presentation.navigation.navigationBuilder
import com.kt.uptodo.presentation.theme.UpTodoTheme
import com.kt.uptodo.utils.Constants.NavigationBarHeight
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var database: UptodoDatabase

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            UpTodoTheme {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()

                LaunchedEffect(Unit) {
                    navController.currentBackStack.collect { entries ->
                        entries.map { entry ->
                            val route = entry.destination.route ?: "null"
                            val duration =
                                entry.arguments?.getLong("duration")?.toString() ?: "no duration"
                            "$route, duration = $duration"
                        }.joinToString("\n").let { logString ->
                            Timber.d("NavigationBackStack: \n$logString")
                        }
                    }
                }


                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    val navigationItems = remember { Screens.MainScreens }
                    val topLevelScreens = listOf(
                        Screens.Index.route,
                        Screens.Calendar.route,
                        Screens.Focus.route,
                        Screens.More.route
                    )

                    val windowInsets = WindowInsets.systemBars
                    val density = LocalDensity.current
                    val bottomInset = with(density) { windowInsets.getBottom(density).toDp() }
                    val topInset = with(density) { windowInsets.getTop(density).toDp() }

                    val shouldShowNavigationBar = remember(backStackEntry) {
                        backStackEntry?.destination?.route == null
                                || navigationItems.fastAny {
                            it.route.substringBefore("?") == backStackEntry?.destination?.route?.substringBefore(
                                "?"
                            )
                        }
                    }

                    val navigationBarHeight by animateDpAsState(
                        targetValue = if (shouldShowNavigationBar) NavigationBarHeight else 0.dp,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium)
                    )

                    val localWindowInset =
                        remember(bottomInset, shouldShowNavigationBar) {
                            var bottom = bottomInset
                            if (shouldShowNavigationBar) bottom += NavigationBarHeight

                            windowInsets
                                .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                                .add(WindowInsets(bottom = bottom))
                        }

                    CompositionLocalProvider(
                        LocalWindowInsets provides localWindowInset,
                        LocalDatabase provides database
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = Screens.Index.route,
                            enterTransition = {
                                if (initialState.destination.route in topLevelScreens
                                    && targetState.destination.route in topLevelScreens
                                ) {
                                    fadeIn(tween(250))
                                } else {
                                    fadeIn(tween(250)) + slideInHorizontally { it / 2 }
                                }
                            },
                            exitTransition = {
                                if (initialState.destination.route in topLevelScreens
                                    && targetState.destination.route in topLevelScreens
                                ) {
                                    fadeOut(tween(200))
                                } else {
                                    fadeOut(tween(200)) + slideOutHorizontally { -it / 2 }
                                }
                            },
                            popEnterTransition = {
                                if ((initialState.destination.route in topLevelScreens
                                            || initialState.destination.route?.startsWith("search/") == true)
                                    && targetState.destination.route in topLevelScreens
                                ) {
                                    fadeIn(tween(250))
                                } else {
                                    fadeIn(tween(250)) + slideInHorizontally { -it / 2 }
                                }
                            },
                            popExitTransition = {
                                if ((initialState.destination.route in topLevelScreens
                                            || initialState.destination.route?.startsWith("search/") == true)
                                    && targetState.destination.route in topLevelScreens
                                ) {
                                    fadeOut(tween(200))
                                } else {
                                    fadeOut(tween(200)) + slideOutHorizontally { it / 2 }
                                }
                            }
                        ) {
                            navigationBuilder(navController)
                        }
                    }

                    NavigationBar(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset {
                                if (navigationBarHeight == 0.dp) {
                                    IntOffset(
                                        x = 0,
                                        y = (bottomInset + NavigationBarHeight).roundToPx()
                                    )
                                } else {
                                    val hideOffset =
                                        (bottomInset + NavigationBarHeight) * (1 - navigationBarHeight / NavigationBarHeight)
                                    IntOffset(x = 0, y = hideOffset.roundToPx())
                                }
                            }
                    ) {
                        navigationItems.fastForEach { screens ->
                            key(screens.route) {
                                NavigationBarItem(
                                    selected = backStackEntry?.destination?.hierarchy?.any {
                                        it.route == screens.route
                                    } == true,
                                    onClick = {
                                        if (backStackEntry?.destination?.hierarchy?.any {
                                                it.route?.substringBefore("?") == screens.route
                                            } == true) {
                                            backStackEntry?.savedStateHandle?.set(
                                                "scrollToTop",
                                                true
                                            )
                                        } else {
                                            navController.navigate(screens.route) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            painter = painterResource(screens.iconId),
                                            contentDescription = null
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = stringResource(screens.titleId),
                                            style = MaterialTheme.typography.bodyMedium,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    },
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}

val LocalWindowInsets =
    compositionLocalOf<WindowInsets> { error("CompositionLocal LocalWindowInset not present") }
val LocalDatabase =
    compositionLocalOf<UptodoDatabase> { error("CompositionLocal LocalDatabase not present") }