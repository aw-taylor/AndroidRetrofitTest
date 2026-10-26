package com.awt.retrofittest.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroApiService {
    @GET("search")
    suspend fun getTracks(
        @Query("term") term: String,
        @Query("country") country: String,
        @Query("entity") entity: String,
        @Query("limit") limit: Int
    ): Response<RetroTracks>
}