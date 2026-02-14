package ru.mactiva.castumritma.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.koin.androidx.compose.koinViewModel
import ru.mactiva.castumritma.ui.screens.details.DetailsScreen
import ru.mactiva.castumritma.ui.screens.player.PlayerScreen
import ru.mactiva.castumritma.ui.screens.search.SearchScreen
import ru.mactiva.castumritma.ui.viewmodels.DetailsViewModel
import ru.mactiva.castumritma.ui.viewmodels.PlayerViewModel
import ru.mactiva.castumritma.ui.viewmodels.SearchViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.mactiva.castumritma.ui.screens.components.MiniPlayer
import ru.mactiva.castumritma.ui.screens.settings.SettingsScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val playerVm: PlayerViewModel = koinViewModel() // Общая VM для навигации и плеера


    Scaffold(
        bottomBar = {
            // Показываем MiniPlayer везде, КРОМЕ экрана Player
            if (currentRoute != Screen.Player.route) {
                MiniPlayer(onClicked = { navController.navigate(Screen.Player.route) })
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Search.route,
            modifier = Modifier.padding(innerPadding) // ПРИМЕНЯЕМ ПАДДИНГ ТУТ
        ) {
            // Экрана поиска
            composable(route = Screen.Search.route) {
                // Koin сам создаст ViewModel и внедрит в неё репозиторий
                val searchViewModel: SearchViewModel = koinViewModel()

                SearchScreen(
                    viewModel = searchViewModel,
                    onPodcastClick = { podcast ->
                        navController.navigate(Screen.Details.createRoute(podcast.id))
                    },
                    onSettingsClick = {
                        navController.navigate("settings") // Убедись, что маршрут "settings" совпадает с тем, что ниже
                    }
                )
            }

            // Экран деталей
            composable(
                route = Screen.Details.route,
                arguments = listOf(
                    navArgument(Screen.Details.ARG_ID) { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val collectionId = backStackEntry.arguments?.getString(Screen.Details.ARG_ID) ?: ""

                // Снова магия Koin: никаких Factory!
                val detailsViewModel: DetailsViewModel = koinViewModel()

                DetailsScreen(
                    collectionId = collectionId,
                    viewModel = detailsViewModel,
                    onBack = { navController.popBackStack() }, // Передаем действие
                    onEpisodeClick = { episode ->              // Передаем действие
                        playerVm.loadAndPlay(episode)
                        navController.navigate(Screen.Player.route)
                    }
                )
            }

            // 3. Экран плеера
            composable(Screen.Player.route) {
                // Берем текущий эпизод прямо из VM
                val currentEpisode by playerVm.currentEpisode.collectAsState()

                currentEpisode?.let { episode ->
                    PlayerScreen(
                        episode = episode,
                        viewModel = playerVm,
                        onBack = { navController.popBackStack() }
                    )
                }
            }

            composable("settings") {
                SettingsScreen(
                    onBack = { navController.popBackStack() }
                )
            }

        }
    }
}


