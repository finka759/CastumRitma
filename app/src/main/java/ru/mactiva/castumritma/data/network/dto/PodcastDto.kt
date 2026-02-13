package ru.mactiva.castumritma.data.network.dto

import ru.mactiva.castumritma.domain.Episode
import ru.mactiva.castumritma.domain.Podcast
import ru.mactiva.castumritma.domain.PodcastSource

data class PodcastResponse(
    val resultCount: Int,
    val results: List<PodcastDto> // Retrofit будет парсить сюда и подкасты, и эпизоды
)

data class PodcastDto(
    val wrapperType: String?, // "track" для эпизода, "collection" для подкаста
    val collectionId: Long,
    val trackId: Long?,          // ДОБАВИТЬ ЭТО ПОЛЕ
    val artistName: String?,
    val collectionName: String?,
    val artworkUrl100: String?,
    val artworkUrl600: String?,
    val primaryGenreName: String?,

    // Поля специально для Эпизодов:
    val trackName: String?,      // Название эпизода
    val description: String?,    // Описание
    val previewUrl: String?,     // Ссылка на mp3 файл
    val releaseDate: String?,    // Дата публикации
    val trackTimeMillis: Long?   // Длительность
) {
    fun toDomain(): Podcast {
        return Podcast(
            id = this.collectionId.toString(),
            title = this.collectionName ?: "Без названия",
            author = this.artistName ?: "Неизвестный автор",
            imageUrl = this.artworkUrl600 ?: this.artworkUrl100,
            genre = this.primaryGenreName ?: "Подкаст",
            source = PodcastSource.ITUNES
        )
    }

    // Добавим маппер для эпизода (на будущее для Domain слоя)
    // Его мы используем в DetailsViewModel
}
//fun PodcastDto.toEpisode(): Episode {
//    return Episode(
//        id = this.trackId ?: 0L, // В iTunes у эпизода это trackId
//        title = this.trackName ?: "Без названия",
//        description = this.description ?: "Описание отсутствует",
//        audioUrl = this.previewUrl ?: "",
//        date = this.releaseDate ?: "",
//        duration = formatDuration(this.trackTimeMillis ?: 0L),
//        imageUrl = this.artworkUrl600 ?: this.artworkUrl100,
//    )
//}
//
//// Вспомогательная функция для красивого отображения времени
//private fun formatDuration(millis: Long): String {
//    val minutes = (millis / 1000) / 60
//    val seconds = (millis / 1000) % 60
//    return String.format("%d:%02d", minutes, seconds)
//}
