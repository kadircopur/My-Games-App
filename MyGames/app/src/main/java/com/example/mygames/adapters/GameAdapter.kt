package com.example.mygames.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mygames.R
import com.example.mygames.activities.DetailPage
import com.example.mygames.models.GameModel

class GameAdapter(
    val context: Context,
    games: ArrayList<GameModel>
    ): RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    private val games: ArrayList<GameModel>
    private var categoryName = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.game_card, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val model: GameModel = games[position]

        Glide.with(context).load(model.background_image).into(holder.gamePic)
        holder.title.setText(model.name)
        holder.score.setText(model.metacritic.toString())
        takeGenres(model)
        holder.category.setText(categoryName)
    }

    override fun getItemCount(): Int {
        return games.size
    }


    private fun takeGenres(model: GameModel){
        categoryName = ""
        model.genres.forEach {categoryName += it.name + "  "}
    }
    // View holder class for initializing of your views such as TextView and Imageview.
    inner class GameViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val gamePic: ImageView = itemView.findViewById(R.id.gameImg)
        val title: TextView = itemView.findViewById(R.id.title)
        val score: TextView = itemView.findViewById(R.id.some_id)
        val category: TextView = itemView.findViewById(R.id.number)
        val gameCard: ConstraintLayout = itemView.findViewById(R.id.cardConstraint)

        init {
            itemView.setOnClickListener {
                val intent = Intent(context, DetailPage::class.java)
                intent.putExtra("gameImgSrc", games[adapterPosition].background_image)
                intent.putExtra("gameName", games[adapterPosition].name)
                intent.putExtra("gameId", games[adapterPosition].id)
                intent.putExtra("metacritic", games[adapterPosition].metacritic)
                takeGenres(games[adapterPosition])
                intent.putExtra("category", categoryName)
                context.startActivity(intent)
            }
        }
    }

    // Constructor
    init {
        this.games = games
    }
}


