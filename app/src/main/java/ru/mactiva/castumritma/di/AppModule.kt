package ru.mactiva.castumritma.di


import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mactiva.castumritma.data.network.api.ITunesApiService
import ru.mactiva.castumritma.data.repository.PodcastRepository
import ru.mactiva.castumritma.data.repository.PodcastRepositoryImpl
import ru.mactiva.castumritma.ui.viewmodels.SearchViewModel
import ru.mactiva.castumritma.ui.viewmodels.DetailsViewModel
import ru.mactiva.castumritma.ui.viewmodels.PlayerViewModel
import ru.mactiva.castumritma.utils.Constants

val appModule = module {
    // Network: Retrofit & API
    single {
        Retrofit.Builder()
            .baseUrl(Constants.ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }

    // Repository: Связываем интерфейс и реализацию
    single<PodcastRepository> { PodcastRepositoryImpl(get()) }
    // Player: Объявляем как single, чтобы он был "вечным"
    singleOf(::PlayerViewModel)

    // ViewModels
    viewModelOf(::SearchViewModel)
    viewModelOf(::DetailsViewModel)


    // Media3 (подготовим заглушку для следующего шага)
    // single { SimpleMediaManager(get()) }
}


