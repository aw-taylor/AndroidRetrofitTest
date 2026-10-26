package com.awt.retrofittest.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import retrofit2.Response

@Serializable
@JsonIgnoreUnknownKeys
data class RetroTrack(
    val artistName: String,
    val collectionName: String,
    val trackName: String,
    val artworkUrl100: String
)

@Serializable
data class RetroTracks(
    val resultCount: Int,
    val results: List<RetroTrack>
)
