package ru.mactiva.castumritma.data.repository

import android.util.Log
import ru.mactiva.castumritma.data.mappers.toEpisodeDomain
import ru.mactiva.castumritma.data.network.api.ITunesApiService
import ru.mactiva.castumritma.domain.Episode
import ru.mactiva.castumritma.domain.Podcast

class PodcastRepositoryImpl(private val apiService: ITunesApiService): PodcastRepository {

    // Твой существующий метод поиска
    override suspend fun searchPodcasts(term: String): List<Podcast> {
        val response = apiService.searchPodcasts(term)
        return response.results.map { it.toDomain() }
    }

    // НОВЫЙ МЕТОД: Получение эпизодов
    override suspend fun fetchEpisodes(collectionId: String): List<Episode> {
        val response = apiService.getEpisodes(collectionId)

// Логируем общее количество объектов
        Log.d("ITUNES_DEBUG", "Всего пришло объектов: ${response.results.size}")

        response.results.forEachIndexed { index, item ->
            Log.d(
                "ITUNES_DEBUG", "Объект #$index: " +
                        "wrapperType=${item.wrapperType}, " +
                        "kind=${item.trackName}, " + // Здесь часто лежит 'podcast-episode'
                        "name=${item.trackName ?: item.collectionName}"
            )
        }

        // Пока что возвращаем всё без фильтрации, чтобы увидеть их на экране
        return response.results
            .filter { it.wrapperType != "collection" } // Убираем только заголовок
            .map { it.toEpisodeDomain() }
    }
}