package ru.mactiva.castumritma.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.mactiva.castumritma.data.repository.PodcastRepository
import ru.mactiva.castumritma.domain.Episode

class DetailsViewModel(
    private val repository: PodcastRepository // Убедись, что он прокинут
) : ViewModel() {

    private val _episodes = MutableStateFlow<List<Episode>>(emptyList())
    val episodes: StateFlow<List<Episode>> = _episodes

    fun loadEpisodes(id: String) {
        viewModelScope.launch {
            try {
                // Здесь будет вызов репозитория, который дернет API
                val result = repository.fetchEpisodes(id)
                _episodes.value = result.filter { it.audioUrl.isNotBlank() }//берем эпизоды только имеющие url
            } catch (e: Exception) {
                // Обработка ошибки
            }
        }
    }
}
