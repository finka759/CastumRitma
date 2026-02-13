package ru.mactiva.castumritma.domain

data class Episode(
    val id: Long,
    val title: String,
    val description: String?,
    val duration: String?, // Потом отформатируем
    val audioUrl: String,
    val date: String,
    val imageUrl: String?
)