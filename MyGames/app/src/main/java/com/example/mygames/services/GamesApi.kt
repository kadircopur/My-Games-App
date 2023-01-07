package com.example.mygames.services

import com.example.mygames.models.GameDetailModel
import com.example.mygames.models.ResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GamesApi {
    @GET("api/games?key=3be8af6ebf124ffe81d90f514e59856c")
    fun getGames(@Query("page_size") pageSize: Int, @Query("page") page: Int): Call<ResponseModel>

    @GET("api/games/{id}?key=3be8af6ebf124ffe81d90f514e59856c")
    fun getGameDetail(@Path("id") id: Int): Call<GameDetailModel>

    @GET("api/games?key=3be8af6ebf124ffe81d90f514e59856c")
    fun getGameSearch(@Query("search") name: String): Call<ResponseModel>
}