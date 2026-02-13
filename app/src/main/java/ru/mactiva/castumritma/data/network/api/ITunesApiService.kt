package ru.mactiva.castumritma.data.network.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.mactiva.castumritma.data.network.dto.PodcastResponse

interface ITunesApiService {
    @GET("search")
    suspend fun searchPodcasts(
        @Query("term") term: String,
        @Query("media") media: String = "podcast",
        @Query("limit") limit: Int = 20
    ): PodcastResponse

    @GET("lookup")
    suspend fun getEpisodes(
        @Query("id") collectionId: String,
        @Query("entity") entity: String = "podcastEpisode",
        @Query("limit") limit: Int = 100
    ): PodcastResponse // Используем ту же обертку, что и для поиска

}