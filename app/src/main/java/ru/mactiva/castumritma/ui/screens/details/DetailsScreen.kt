package ru.mactiva.castumritma.ui.screens.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import ru.mactiva.castumritma.R
import ru.mactiva.castumritma.domain.Episode
import ru.mactiva.castumritma.ui.viewmodels.DetailsViewModel
import ru.mactiva.castumritma.ui.viewmodels.PlayerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    collectionId: String,
    viewModel: DetailsViewModel = koinViewModel(), // Передаем сюда нашу ViewModel
    onBack: () -> Unit,
    onEpisodeClick: (Episode) -> Unit // Добавляем этот callback
) {
    // для подсветки проигрываемого эпизода
    val playerVm: PlayerViewModel = koinViewModel()
    val currentPlaying by playerVm.currentEpisode.collectAsState()

// Запускаем загрузку эпизодов при первом запуске экрана
    LaunchedEffect(collectionId) {
        viewModel.loadEpisodes(collectionId)
    }

    // 2. Подписываемся на поток эпизодов из ViewModel
    val episodes by viewModel.episodes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.episodes_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        if (episodes.isEmpty()) {
            // 3. Показываем лоадер, пока список пуст
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // 4. Отображаем список, когда данные пришли
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                items(episodes) { episode ->
                    val isActive = currentPlaying?.id != null && episode.id == currentPlaying?.id
                    EpisodeItem(
                        episode = episode,
                        isActive = isActive, // Передаем флаг
                        onClick = { onEpisodeClick(episode) } // Вызываем при клике
                    )
                }
            }
        }
    }
}

@Composable
fun EpisodeItem(episode: Episode, isActive: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            // ДОБАВЛЕНО: Теперь вся карточка кликабельна
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            // Если активен — подсвечиваем цветом бренда
            containerColor = if (isActive) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        ),
        border = if (isActive) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Отображаем картинку из твоего маппера
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp)) // Чуть больше скругление
                    .background(MaterialTheme.colorScheme.primaryContainer), // Цвет бренда
                contentAlignment = Alignment.Center
            ) {
                // Фоновые инициалы проекта
                Text(
                    text = "CRa",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.alpha(0.5f) // Делаем текст полупрозрачным фоном
                )

                AsyncImage(
                    model = episode.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = episode.title.ifEmpty { stringResource(R.string.no_title) },
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // ИСПРАВЛЕНО: Теперь просто берем готовую дату из маппера
                    Text(
                        text = episode.date,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    val duration = episode.duration
                    if (!duration.isNullOrEmpty()) {
                        Text(text = " • ", style = MaterialTheme.typography.labelSmall)
                        Text(
                            text = stringResource(R.string.duration_label, duration),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    if (isActive) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp, // Иконка динамика
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
