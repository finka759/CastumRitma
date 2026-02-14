package ru.mactiva.castumritma.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import ru.mactiva.castumritma.R
import ru.mactiva.castumritma.ui.viewmodels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    onBack: () -> Unit
) {
    // Используем делегат by для удобства (нужен импорт getValue)
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        // Исправлено: обычный Icon вместо SegmentedButtonDefaults
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 1. Переключатель Темы (Material 3 ListItem)
            ListItem(
                headlineContent = { Text(stringResource(R.string.theme_setting)) },
                trailingContent = {
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { viewModel.toggleTheme(it) }
                    )
                }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // 2. Выбор Языка
            Text(
                text = stringResource(R.string.language_setting),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Button(onClick = { viewModel.changeLanguage("ru") }) {
                    Text(stringResource(R.string.lang_ru))
                }
                Spacer(Modifier.width(12.dp))
                Button(onClick = { viewModel.changeLanguage("en") }) {
                    Text(stringResource(R.string.lang_en))
                }
            }
        }
    }
}
