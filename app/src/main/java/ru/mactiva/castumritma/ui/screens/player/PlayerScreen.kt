package ru.mactiva.castumritma.ui.screens.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon                // Убедись, что импорт есть
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import ru.mactiva.castumritma.utils.formatTime
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Surface

@OptIn(ExperimentalMaterial3Api::class) // Нужен для TopAppBar
@Composable
fun PlayerScreen(
    episode: Episode,
    viewModel: PlayerViewModel = koinViewModel(),
    onBack: () -> Unit
) {


    val snackbarHostState = remember { SnackbarHostState() }
    val playbackError by viewModel.playbackError.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()

    // Показываем Snackbar, когда появляется ошибка
    LaunchedEffect(playbackError) {
        playbackError?.let {
            snackbarHostState.showSnackbar(message = it, duration = SnackbarDuration.Long)
//            viewModel.clearError() // Очищаем стейт после показа
        }
    }

    LaunchedEffect(episode) {
        viewModel.loadAndPlay(episode)
    }

    // 1. Оборачиваем в Scaffold, чтобы фон стал правильного цвета (темный в темной теме)
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Playing Now") }, // Можно локализовать позже
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding) // Учитываем высоту TopAppBar
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                // 1. Обложка
                AsyncImage(
                    model = episode.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            if (playbackError != null) Modifier.blur(10.dp) else Modifier // Блюрим обложку при ошибке
                        ),
                    contentScale = ContentScale.Crop
                )
                // 2. Слой ошибки ПОВЕРХ обложки
                playbackError?.let { errorText ->
                    Surface(
                        color = Color.Black.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = errorText,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }



            Spacer(modifier = Modifier.height(24.dp))

            // 2. Название (с цветом из темы)
            Text(
                text = episode.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface, // Явный цвет текста
                textAlign = TextAlign.Center
            )

            // 2. Блок настроек (ТВОЯ ФИШКА)
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Режим воспроизведения",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary // Выделим заголовки цветом
            )

            // Здесь будут кнопки выбора режимов...

            // Внутри Column в PlayerScreen, под названием эпизода
            val currentPos by viewModel.currentPosition.collectAsState()
            val totalDuration by viewModel.duration.collectAsState()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Slider(
                    value = currentPos.toFloat(),
                    onValueChange = { viewModel.seekTo(it.toLong()) },
                    valueRange = 0f..totalDuration.toFloat().coerceAtLeast(1f),
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = currentPos.formatTime(),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = totalDuration.formatTime(),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }


            Spacer(modifier = Modifier.weight(1f)) // Толкаем управление вниз

            // 3. Управление
            val isPlaying by viewModel.isPlaying.collectAsState()

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { viewModel.togglePlayPause() },
                    modifier = Modifier.size(80.dp) // Увеличим кнопку для удобства
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
