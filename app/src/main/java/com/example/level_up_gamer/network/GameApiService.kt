package com.example.level_up_gamer.network

import com.example.level_up_gamer.model.RemoteGame
import retrofit2.http.GET
import retrofit2.http.Query

interface GameApiService {
    @GET("api/games")
    suspend fun getVideoGames(
        @Query("platform") platform: String = "pc"
    ): List<RemoteGame>
}

