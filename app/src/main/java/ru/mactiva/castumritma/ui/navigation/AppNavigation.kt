package ru.mactiva.castumritma.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
// ВАЖНО: Используем Koin для Compose
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

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {

    val playerVm: PlayerViewModel = koinViewModel() // Общая VM для навигации и плеера

    NavHost(
        navController = navController,
        startDestination = Screen.Search.route
    ) {
        // Экрана поиска
        composable(route = Screen.Search.route) {
            // Koin сам создаст ViewModel и внедрит в неё репозиторий
            val searchViewModel: SearchViewModel = koinViewModel()

            SearchScreen(
                viewModel = searchViewModel,
                onPodcastClick = { podcast ->
                    navController.navigate(Screen.Details.createRoute(podcast.id))
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

    }
}


