package ru.mactiva.castumritma.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.HttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.mactiva.castumritma.domain.Episode

class PlayerViewModel(
    private val player: ExoPlayer
) : ViewModel() {

    // Состояние плеера для UI
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _currentEpisode = MutableStateFlow<Episode?>(null)
    val currentEpisode = _currentEpisode.asStateFlow()


    private val _currentPosition = MutableStateFlow(0L)//Для прогресса проигрывания
    val currentPosition = _currentPosition.asStateFlow()//Для прогресса проигрывания

    private val _duration = MutableStateFlow(0L)//Для прогресса проигрывания
    val duration = _duration.asStateFlow()//Для прогресса проигрывания

    private val _playbackError = MutableStateFlow<String?>(null)
    val playbackError = _playbackError.asStateFlow()

    // Очистка ошибки (например, при закрытии уведомления)
    fun clearError() { _playbackError.value = null }


    init {
        // Слушаем состояние плеера в реальном времени
        player.addListener(object : androidx.media3.common.Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
                if (isPlaying) startProgressUpdate()
            }

            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    _duration.value = player.duration.coerceAtLeast(0L)
                }
                if (state == Player.STATE_BUFFERING) _playbackError.value = null
            }
            override fun onPlayerError(error: PlaybackException) {
                val cause = error.cause
                var serverInfo = ""

                // Проверяем именно на InvalidResponseCodeException
                if (cause is HttpDataSource.InvalidResponseCodeException) {
                    val code = cause.responseCode
                    val message = cause.message ?: ""
                    serverInfo = "\n(HTTP $code: ${message.take(30)})" // Берем первые 30 символов сообщения
                }

                _playbackError.value = when (error.errorCode) {
                    PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS -> {
                        "Доступ ограничен.$serverInfo\nКонтент недоступен в вашем регионе."
                    }
                    PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> "Ошибка сети$serverInfo"
                    else -> "Ошибка загрузки$serverInfo"
                }
            }

        })
    }

    //для логики отслеживания времени и прогресса проигрывания
    private fun startProgressUpdate() {
        viewModelScope.launch {
            // Пока плеер играет, обновляем позицию каждую секунду
            while (_isPlaying.value) {
                _currentPosition.value = player.currentPosition
                delay(1000)// Тикаем раз в секунду
            }
        }
    }

    //для логики отслеживания времени и прогресса проигрывания
    fun seekTo(position: Long) {
        player.seekTo(position)
        _currentPosition.value = position
    }

    fun loadAndPlay(episode: Episode) {
        _playbackError.value = null
        println("P_LOG: Пытаемся включить: ${episode.title}, URL: ${episode.audioUrl}")
        // Если это ТОТ ЖЕ самый эпизод — ничего не делаем (пусть играет дальше)
        if (player.currentMediaItem?.mediaId == episode.audioUrl) return

        // Сбрасываем прогресс-бар в ноль для UI, чтобы он не "прыгал"
        _currentPosition.value = 0L
        // Сохраняем эпизод для UI
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

        //Явно указываем плееру начать с начала (0 мс)
        player.seekTo(0L)

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
