package com.awt.retrofittest.data

import com.awt.retrofittest.network.RetroApiService
import com.awt.retrofittest.network.RetroTracks
import retrofit2.Response

interface RetroRepository {
    suspend fun getTracks(
        term: String,
        country: String,
        limit: Int
    ): Response<RetroTracks>
}

class NetworkRetroRepository(
    private val retroApiService: RetroApiService
) : RetroRepository {
    override suspend fun getTracks(
        term: String,
        country: String,
        limit: Int
    ): Response<RetroTracks> = retroApiService.getTracks(
        term = term,
        country = country,
        entity = "song",
        limit = limit
    )
}