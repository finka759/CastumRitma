package ru.mactiva.castumritma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.mactiva.castumritma.ui.theme.CastumRitmaTheme
import androidx.navigation.compose.rememberNavController
import ru.mactiva.castumritma.data.network.RetrofitClient
import ru.mactiva.castumritma.data.repository.PodcastRepositoryImpl
import ru.mactiva.castumritma.ui.navigation.AppNavigation


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        // Оборачиваем в тему, чтобы работали цвета Material 3
        setContent {
            CastumRitmaTheme {
                AppNavigation()
            }
        }
    }
}



