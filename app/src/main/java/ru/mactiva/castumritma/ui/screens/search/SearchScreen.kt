package ru.mactiva.castumritma.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* // Для Column, fillMaxSize, padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Важно для работы items(podcasts)
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text // Нужно импортировать отдельно
import androidx.compose.runtime.* // Для getValue/setValue и remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage // Импорт из Coil
import androidx.compose.ui.Alignment
import org.koin.androidx.compose.koinViewModel
import ru.mactiva.castumritma.domain.Podcast
import ru.mactiva.castumritma.ui.viewmodels.SearchViewModel


@Composable
fun SearchScreen(
    onPodcastClick: (ru.mactiva.castumritma.domain.Podcast) -> Unit,
    viewModel: SearchViewModel = koinViewModel()
) { // Нужна зависимость androidx.lifecycle.viewmodel.compose
    val searchQuery by viewModel.searchQuery.collectAsState()
    val state by viewModel.podcasts.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onQueryChange(it) }, // Отправляем изменения во ViewModel
            label = { Text("Поиск подкастов") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp) // Отступ снизу
        ) {
            items(state) { podcast ->
                PodcastItem(
                    podcast = podcast,
                    modifier = Modifier.clickable {
                        onPodcastClick(podcast) // 2. ВЫЗОВ ОБЯЗАТЕЛЕН
                    }
                )
            }
        }
    }
}

@Composable
fun PodcastItem(
    podcast: Podcast,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ОБЛОЖКА: Теперь берем поле imageUrl из нашей Domain-модели
            AsyncImage(
                model = podcast.imageUrl,
                contentDescription = "Podcast Cover",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = podcast.title, // В модели Podcast это title (вместо collectionName)
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Text(
                    text = podcast.author, // В модели Podcast это author (вместо artistName)
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}



