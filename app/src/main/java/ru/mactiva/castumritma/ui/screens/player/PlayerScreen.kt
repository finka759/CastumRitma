package ru.mactiva.castumritma.ui.screens.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import ru.mactiva.castumritma.domain.Episode
import ru.mactiva.castumritma.ui.viewmodels.PlayerViewModel
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.filled.PlayArrow // Нужно добавить
import androidx.compose.material3.Icon                // Убедись, что импорт есть
import androidx.compose.runtime.getValue

@Composable
fun PlayerScreen(
    episode: Episode, // Получаем из навигации
    viewModel: PlayerViewModel = koinViewModel(),
    onBack: () -> Unit
) {
    // Автозапуск при входе на экран
    LaunchedEffect(episode) {
        viewModel.loadAndPlay(episode)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Обложка и Название
        AsyncImage(model = episode.imageUrl, contentDescription = null, modifier = Modifier.size(300.dp).clip(RoundedCornerShape(16.dp)))
        Text(text = episode.title, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(top = 16.dp))

        // 2. Блок настроек (ТВОЯ ФИШКА)
        Spacer(modifier = Modifier.height(32.dp))
        Text("Режим воспроизведения", style = MaterialTheme.typography.titleMedium)
        // Тут будут кнопки: [Папка] [Случайно] [Плейлист]

        // 3. Управление
        Row(verticalAlignment = Alignment.CenterVertically) {

            val isPlaying by viewModel.isPlaying.collectAsState() // Выносим для чистоты кода

            IconButton(onClick = { viewModel.togglePlayPause() }) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary // Добавим акцентный цвет
                )
            }
        }
    }
}
