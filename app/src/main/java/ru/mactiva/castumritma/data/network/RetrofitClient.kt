package ru.mactiva.castumritma.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mactiva.castumritma.data.network.api.ITunesApiService

object RetrofitClient {
    private const val BASE_URL = "https://itunes.apple.com"

    val apiService: ITunesApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }
}