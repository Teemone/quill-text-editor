package com.yourandroidguy.quil

import android.util.Log
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yourandroidguy.quil.components.LargeHomeFab
import com.yourandroidguy.quil.screens.HomeScreen
import com.yourandroidguy.quil.screens.NoteEntryScreen
import com.yourandroidguy.quil.ui.theme.QuilTheme
import com.yourandroidguy.quil.viewmodel.QuilViewModel

@Composable
fun App(
    viewModel: QuilViewModel
) {
    QuilTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination


        Scaffold(
            floatingActionButton = {
                if (currentDestination?.route == HomeScreen.route)
                    LargeHomeFab(
                        icon = painterResource(id = R.drawable.quil_no_fill),
                        onClick = {navController.navigateNoteEntry() })
            }
        ) { pv ->
            val layoutDirection = LocalLayoutDirection.current
            Column{
                QuilNavHost(
                    navHostController = navController,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(
                            start = pv.calculateStartPadding(layoutDirection),
                            end = pv.calculateEndPadding(layoutDirection)
                        ),
                    viewModel = viewModel
                )
            }

        }
    }
}

@Composable
fun QuilNavHost(
    viewModel: QuilViewModel,
    navHostController: NavHostController,
    modifier: Modifier=Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = HomeScreen.route,
        enterTransition = {
            fadeIn() + slideIn(
                initialOffset = {
                    IntOffset(it.width, it.height)
                }
            )
        },
        exitTransition = {
            fadeOut() + slideOut(
                targetOffset = {
                    IntOffset(-it.width, -it.height)
                }
            )
        },
        popEnterTransition = {
            fadeIn() + slideIn(
                initialOffset = {
                    IntOffset(-it.width, -it.height)
                }
            )
        },
        popExitTransition = {
            fadeOut() + slideOut(
                targetOffset = {
                    IntOffset(it.width, it.height)
                }
            )
        }
    ) {
        composable(HomeScreen.route) {
            val noteList = viewModel.getAllRecords().collectAsStateWithLifecycle(initialValue = listOf()).value

            HomeScreen(
                viewModel = viewModel,
                noteList = noteList,
                onNoteItemClicked = {
                    navHostController.navigateNoteEntry(it.id)
                    Log.i("NOTE ID", it.id.toString())
                }
            )
        }
        composable(
            route = NoteEntry.routeWithArg,
            arguments = NoteEntry.arguments
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong(NoteEntry.ARG_NAME) ?: -1

            NoteEntryScreen(
                noteId = id,
                navController = navHostController,
                viewModel = viewModel
            )
        }
    }

}

fun NavHostController.navigateSingleTop(route: String){
    this.navigate(route){
        popUpTo(
            this@navigateSingleTop.graph.findStartDestination().id
        ){saveState = true}
        restoreState = true
        launchSingleTop = true
    }
}

fun NavHostController.navigateNoteEntry(id: Long = -1){
    this.navigateSingleTop("${NoteEntry.route}/${id}")
}