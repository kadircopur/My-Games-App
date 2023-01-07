package com.example.mygames.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mygames.R

class EmptyPageAdapter(
    private val context: Context,
    emptyMessage: ArrayList<String>
): RecyclerView.Adapter<EmptyPageAdapter.ViewHolder>(){

    private val emptyMessage: ArrayList<String>

    init {
        this.emptyMessage = emptyMessage
    }

    inner class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val emptyPageMessage: TextView

        init {
            emptyPageMessage = itemView.findViewById(R.id.empty_page_message)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.empty_page, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message: String = emptyMessage[position]
        holder.emptyPageMessage.setText(message)
    }

    override fun getItemCount(): Int {
        return emptyMessage.size
    }
}