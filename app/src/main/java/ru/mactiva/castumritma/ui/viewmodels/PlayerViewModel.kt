package ru.mactiva.castumritma.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.mactiva.castumritma.domain.Episode

class PlayerViewModel(
    private val player: ExoPlayer
) : ViewModel() {

    // Состояние плеера для UI
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _currentEpisode = MutableStateFlow<Episode?>(null)
    val currentEpisode = _currentEpisode.asStateFlow()

    init {
        // Слушаем состояние плеера в реальном времени
        player.addListener(object : androidx.media3.common.Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
            }
        })
    }

    fun loadAndPlay(episode: Episode) {
        println("P_LOG: Пытаемся включить: ${episode.title}, URL: ${episode.audioUrl}")

        // 1. Сначала сохраняем эпизод для UI
        _currentEpisode.value = episode

        // 2. Защита от пустого URL
        if (episode.audioUrl.isBlank()) {
            println("P_LOG: ОШИБКА: URL пустой!")
            return
        }

        // 3. Проверка на дубликат (чтобы не прерывать текущую песню)
        if (player.currentMediaItem?.mediaId == episode.audioUrl) return

        // 4. Явное создание Uri
        val audioUri = Uri.parse(episode.audioUrl)

        val mediaItem = MediaItem.Builder()
            .setMediaId(episode.audioUrl)
            .setUri(audioUri) // Передаем объект Uri, а не String!
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(episode.title)
                    .setArtworkUri(Uri.parse(episode.imageUrl))
                    .build()
            )
            .build()

        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
        // _isPlaying обновится через Listener в init автоматически
    }

    fun togglePlayPause() {
        if (player.isPlaying) player.pause() else player.play()

    }

    override fun onCleared() {
        // Плеер не релизим здесь, так как он в синглтоне di
        super.onCleared()
    }
}
