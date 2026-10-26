package com.awt.retrofittest.data

import com.awt.retrofittest.network.RetroApiService
import com.awt.retrofittest.network.RetroTracks
import retrofit2.Response

interface RetroRepository {
    suspend fun getTracks(term: String): Response<RetroTracks>
}

class NetworkRetroRepository(
    private val retroApiService: RetroApiService
) : RetroRepository {
    override suspend fun getTracks(term: String): Response<RetroTracks> = retroApiService.getTracks(
        term = term,
        country = "DE",
        entity = "song",
        limit = 24
    )
}