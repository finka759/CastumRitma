package ru.mactiva.castumritma.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.mactiva.castumritma.data.repository.PodcastRepository
import ru.mactiva.castumritma.domain.Podcast

class SearchViewModel(
    private val repository: PodcastRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // Теперь храним список объектов PodcastDto вместо строк
    private val _podcasts = MutableStateFlow<List<Podcast>>(emptyList()) // Тут Podcast!
    val podcasts: StateFlow<List<Podcast>> = _podcasts

    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        if (newQuery.length > 2) { // Начинаем поиск от 3-х символов
            performSearch(newQuery)
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            try {
                val results = repository.searchPodcasts(query)
                _podcasts.value = results
            } catch (e: Exception) {
                // Здесь в будущем добавим обработку ошибок
                _podcasts.value = emptyList()
            }
        }
    }
}
