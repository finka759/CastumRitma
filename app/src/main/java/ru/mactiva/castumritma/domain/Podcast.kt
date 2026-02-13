package ru.mactiva.castumritma.domain

// Этот класс — "золотой стандарт" данных внутри твоего приложения
data class Podcast(
    val id: String,
    val title: String,
    val author: String,
    val imageUrl: String?,
    val genre: String?,
    val source: PodcastSource // Добавим метку источника
)

enum class PodcastSource {
    ITUNES, SPOTIFY, RSS, LOCAL
}