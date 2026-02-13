package ru.mactiva.castumritma

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.mactiva.castumritma.data.mappers.toEpisodeDomain // Проверь свой путь к мапперу!
import ru.mactiva.castumritma.data.network.dto.PodcastDto

class MappersTest {

    @Test
    fun `toEpisodeDomain maps DTO to Domain correctly`() {
        val dto = PodcastDto(
            trackId = 123L,
            trackName = "Test Episode",
            description = "Test Desc",
            previewUrl = "https://audio.mp3",
            releaseDate = "2024-02-13T12:00:00Z",
            trackTimeMillis = 60000L,
            artworkUrl600 = "https://image.jpg",
            // Заменяем TODO() на реальные пустые значения
            wrapperType = "",
            collectionId = 0L,
            artistName = "",
            collectionName = "",
            artworkUrl100 = "",
            primaryGenreName = ""
        )

        val domain = dto.toEpisodeDomain()

        assertEquals(123L, domain.id)
        assertEquals("Test Episode", domain.title)
        // ВНИМАНИЕ: Проверь, что твой маппер реально выдает "1:00" или "1 min"
        // Если маппер выдает "01:00", то замени строку ниже на "01:00"
        assertEquals("1:00", domain.duration)
    }

}

