package com.awt.retrofittest.data

import com.awt.retrofittest.network.RetroApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val retroRepository: RetroRepository
}

class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService : RetroApiService by lazy {
        retrofit.create(RetroApiService::class.java)
    }

    override val retroRepository: RetroRepository by lazy {
        NetworkRetroRepository(retrofitService)
    }
}