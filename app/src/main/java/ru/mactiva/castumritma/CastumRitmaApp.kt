package ru.mactiva.castumritma

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.mactiva.castumritma.di.appModule // Путь к твоему модулю
import ru.mactiva.castumritma.di.playerModule

class CastumRitmaApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Передаем контекст приложения в Koin
            androidContext(this@CastumRitmaApp)
            // Загружаем наши зависимости
            modules(appModule, playerModule)
        }
    }
}