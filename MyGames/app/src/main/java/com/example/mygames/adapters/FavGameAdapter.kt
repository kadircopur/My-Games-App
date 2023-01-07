package com.example.mygames.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mygames.R
import com.example.mygames.favGameList
import com.example.mygames.models.GameModel


class FavGameAdapter(
    private val context: Context,
    games: ArrayList<GameModel>
    ): RecyclerView.Adapter<FavGameAdapter.GameViewHolder>() {

    private val games: ArrayList<GameModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.game_card, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val model: GameModel = games[position]
        var categoryName = ""
        Glide.with(context).load(model.background_image).into(holder.gamePic)
        holder.title.setText(model.name)
        holder.score.setText(model.metacritic.toString())
        model.genres.forEach {categoryName += it.name + "  "}
        holder.category.setText(categoryName)
        holder.gameCard.setBackgroundColor(ContextCompat.getColor(context, R.color.backColor))

    }

    override fun getItemCount(): Int {
        return games.size
    }

    fun deleteItem(index: Int) {
        favGameList.removeAt(index)
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
            }
        }
    }

    // Constructor
    init {
        this.games = games
    }
}