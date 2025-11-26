package com.example.level_up_gamer.model

import com.squareup.moshi.Json

data class RemoteGame(
    val id: Int? = null,
    val title: String? = null,
    val genre: String? = null,
    val platform: String? = null,
    val publisher: String? = null,
    val developer: String? = null,
    @Json(name = "short_description") val shortDescription: String? = null,
    @Json(name = "release_date") val releaseDate: String? = null,
    @Json(name = "game_url") val gameUrl: String? = null,
    @Json(name = "freetogame_profile_url") val profileUrl: String? = null
)

