package com.example.mygames.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.mygames.*
import com.example.mygames.models.GameDetailModel
import com.example.mygames.models.GameModel
import com.example.mygames.models.GenreModel
import com.example.mygames.services.GamesApi
import com.example.mygames.services.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates


class DetailPage : AppCompatActivity() {

    private lateinit var backButton: Button
    private lateinit var favButton: Button
    private lateinit var gameImage: ImageView
    private lateinit var desc: TextView
    private lateinit var reddit: TextView
    private lateinit var redditURL: String
    private lateinit var webSite: TextView
    private lateinit var webURL: String
    private var id by Delegates.notNull<Int>()
    private lateinit var imageURL: String
    private lateinit var name: String
    private var metacritic: Int? = null
    private var genreList: List<GenreModel> = listOf(GenreModel(""))
    private lateinit var gameName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_page)
        genreList.elementAt(0).name = intent.extras?.get("category") as String
        bindViews()
        initListeners(this)

        val apiService = RetrofitInstance.apiService.create(GamesApi::class.java)
        val call = apiService.getGameDetail(id)

        call.enqueue(object: Callback<GameDetailModel> {
            override fun onResponse(call: Call<GameDetailModel>, response: Response<GameDetailModel>) {
                if (response.isSuccessful){
                    val resultList = response.body()
                    if (resultList != null) {
                        webURL = resultList.website
                        redditURL = resultList.reddit_url
                        desc.text = resultList.description_raw
                    }
                }
            }

            override fun onFailure(call: Call<GameDetailModel>, t: Throwable) {
                return }
        })
    }

    fun bindViews() {
        id = intent.extras?.get("gameId") as Int
        backButton = findViewById(R.id.game_pg_button)
        favButton = findViewById(R.id.fav_pg_button)
        if (isFaved()){favButton.text = "Favourıted"}
        metacritic = intent.extras?.get("metacritic") as Int?
        gameImage = findViewById(R.id.gameImage)
        imageURL = intent.extras?.get("gameImgSrc") as String
        Glide.with(this).load(imageURL).into(gameImage)

        reddit = findViewById(R.id.reddit)
        webSite = findViewById(R.id.web_site)
        desc = findViewById(R.id.description)

        gameName = findViewById(R.id.gameName)
        name = intent.extras?.get("gameName").toString()
        gameName.text = name

    }

    fun initListeners(context: Context){
        favButton.setOnClickListener {
            if(!isFaved()){
                favButton.text = "Favourıted"
                favGameList.add(GameModel(id, name, imageURL, metacritic, genreList))
            }
        }

        backButton.setOnClickListener {
            val newIntent = Intent(context, MainActivity::class.java)
            context.startActivity(newIntent)
        }

        reddit.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(redditURL)
            startActivity(intent)
        }

        webSite.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(webURL)
            startActivity(intent)
        }
    }

    // Check the is game favourited
    fun isFaved(): Boolean {
        favGameList.forEach {
            if (id == it.id){
                return true
            }
        }
        return false
    }
}