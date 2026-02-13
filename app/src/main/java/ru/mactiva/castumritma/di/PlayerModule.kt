package ru.mactiva.castumritma.di

import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val playerModule = module {
    // ExoPlayer: один экземпляр на всё приложение
    single {
        // Создаем AudioAttributes отдельно для чистоты кода
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

        ExoPlayer.Builder(androidContext())
            .setAudioAttributes(
                audioAttributes,
                true
            ) // true здесь и есть "handle audio attributes"
            .build()
    }
}
