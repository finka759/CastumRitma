package ru.mactiva.castumritma

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ru.mactiva.castumritma.ui.theme.CastumRitmaTheme
import org.koin.androidx.compose.koinViewModel
import ru.mactiva.castumritma.ui.navigation.AppNavigation
import ru.mactiva.castumritma.ui.viewmodels.SettingsViewModel


class MainActivity :  AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // Получаем ту же самую ViewModel, что и в настройках
            val settingsVm: SettingsViewModel = koinViewModel()
            val isDarkTheme by settingsVm.isDarkTheme.collectAsState()

            // Передаем состояние в тему проекта
            CastumRitmaTheme(darkTheme = isDarkTheme) {
                AppNavigation()
            }
        }
    }
}



