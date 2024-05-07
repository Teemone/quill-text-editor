package com.yourandroidguy.quil

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface QuilDestinations {
    val route: String
}

object HomeScreen: QuilDestinations{
    override val route: String
        get() = "home"
}

object NoteEntry: QuilDestinations{
    override val route: String
        get() = "note_entry"
    const val ARG_NAME = "note_entry_arg"
    val routeWithArg = "$route/{$ARG_NAME}"
    val arguments = listOf(
        navArgument(ARG_NAME){
            type = NavType.LongType
            defaultValue = -1
        }
    )
}