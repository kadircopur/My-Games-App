package com.example.mygames.models

import android.text.BoringLayout


class GameModel(
    id: Int,
    name: String,
    background_image: String,
    metacritic: Int?,
    genres: List<GenreModel>
){
    val id: Int
    val name: String
    val background_image: String
    val metacritic: Int?
    val genres: List<GenreModel>

    init {
        this.id = id
        this.name = name
        this.background_image = background_image
        this.metacritic = metacritic
        this.genres = genres

    }

}