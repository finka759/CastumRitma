package ru.mactiva.castumritma.data.playback

import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import org.koin.android.ext.android.inject // НУЖНЫЙ ИМПОРТ

class PlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null

    // Теперь inject() будет доступен, так как Service — это контекст Android
    private val player: ExoPlayer by inject()

    override fun onCreate() {
        super.onCreate()
        // Проверяем, не инициализирована ли уже сессия, и создаем её
        mediaSession = MediaSession.Builder(this, player).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}
