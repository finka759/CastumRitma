package ru.mactiva.castumritma.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import ru.mactiva.castumritma.ui.viewmodels.PlayerViewModel

@Composable
fun MiniPlayer(
    viewModel: PlayerViewModel = koinViewModel(),
    onClicked: () -> Unit // Клик для перехода в полноэкранный плеер
) {
    val currentEpisode by viewModel.currentEpisode.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()

    if (currentEpisode == null) return // Если ничего не выбрано — не рисуем

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(8.dp)
            .clickable { onClicked() },
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = currentEpisode?.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Column(modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)) {
                Text(
                    currentEpisode?.title ?: "",
                    maxLines = 1,
                    style = MaterialTheme.typography.labelLarge
                )
                Text("Сейчас играет", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = { viewModel.togglePlayPause() }) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null
                )
            }
        }
    }
}