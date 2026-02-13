package ru.mactiva.castumritma.data.mappers

import ru.mactiva.castumritma.data.network.dto.PodcastDto
import ru.mactiva.castumritma.domain.Episode
import ru.mactiva.castumritma.domain.Podcast
import ru.mactiva.castumritma.domain.PodcastSource
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun PodcastDto.toPodcastDomain(): Podcast {
    return Podcast(
        id = this.collectionId.toString(),
        title = this.collectionName ?: "Без названия",
        author = this.artistName ?: "Неизвестный автор",
        imageUrl = this.artworkUrl600 ?: this.artworkUrl100,
        genre = this.primaryGenreName ?: "Подкаст",
        source = PodcastSource.ITUNES
    )
}

// 1. Создаем форматтер (например: 12 окт. 2023)
private val outputFormatter = DateTimeFormatter.ofPattern("d MMM yyyy",  Locale.getDefault())

fun PodcastDto.toEpisodeDomain(): Episode {
    return Episode(
        id = this.trackId ?: 0L,
        title = this.trackName ?: "",
        description = this.description ?: "",
        audioUrl = this.previewUrl ?: "",
        // 2. Используем функцию преобразования вместо простого take(10)
        date = formatDate(this.releaseDate),
        duration = formatDuration(this.trackTimeMillis ?: 0L),
        imageUrl = this.artworkUrl600 ?: this.artworkUrl100
    )
}

// 3. Сама функция парсинга
private fun formatDate(isoDate: String?): String {
    if (isoDate.isNullOrEmpty()) return ""
    return try {
        // Парсим ISO строку (например 2023-10-12T10:00:00Z)
        val parsedDate = ZonedDateTime.parse(isoDate)
        // Форматируем в красивый вид
        val formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault())
        parsedDate.format(formatter)
    } catch (e: Exception) {
        // Если формат пришел битый, просто обрезаем до YYYY-MM-DD
        isoDate.take(10)
    }
}

private fun formatDuration(millis: Long): String {
    if (millis <= 0) return ""
    val minutes = (millis / 1000) / 60
    val seconds = (millis / 1000) % 60
    return String.format("%d:%02d", minutes, seconds)
}
