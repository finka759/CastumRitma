package ru.mactiva.castumritma.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    // Экран поиска (главный)
    object Search : Screen("search")

    // Экран деталей (требует ID коллекции)
    object Details : Screen("details/{collectionId}") {
        const val ARG_ID = "collectionId"
        fun createRoute(id: String) = "details/$id"
    }

    object Player : Screen("player")
}
