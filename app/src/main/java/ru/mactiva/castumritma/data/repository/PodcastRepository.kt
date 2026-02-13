package ru.mactiva.castumritma.data.repository

import ru.mactiva.castumritma.domain.Episode
import ru.mactiva.castumritma.domain.Podcast

interface PodcastRepository {
    suspend fun searchPodcasts(term: String): List<Podcast>
    suspend fun fetchEpisodes(collectionId: String): List<Episode>
}

