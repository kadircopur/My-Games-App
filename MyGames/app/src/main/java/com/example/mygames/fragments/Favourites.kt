package com.example.mygames.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mygames.R
import com.example.mygames.adapters.EmptyPageAdapter
import com.example.mygames.adapters.FavGameAdapter
import com.example.mygames.favGameList
import com.example.mygames.services.SwipeToDeleteCallback

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Favourites.newInstance] factory method to
 * create an instance of this fragment.
 */
class Favourites : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: FavGameAdapter
    private lateinit var emptyAdapter: EmptyPageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Favourites.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Favourites().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textView: TextView = view.findViewById(R.id.fav_title)

        recyclerView = view.findViewById(R.id.fav_game_recyc)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        val emptyMessage: ArrayList<String> = ArrayList<String>()
        emptyMessage.add("There is no favourites found")
        emptyAdapter = context?.let { EmptyPageAdapter(it, emptyMessage) }!!
        adapter = context?.let { FavGameAdapter(it, favGameList) }!!

        setAdapter(textView)

        val swipeDelete = object: SwipeToDeleteCallback(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val builder = android.app.AlertDialog.Builder(context)
                //set title for alert dialog
                builder.setTitle("DELETE GAME !")
                //performing positive action
                builder.setPositiveButton("Yes"){dialogInterface, which ->
                    adapter.deleteItem(viewHolder.adapterPosition)
                    setAdapter(textView)
                }
                //performing negative action
                builder.setNegativeButton("No"){dialogInterface, which ->
                    adapter.notifyDataSetChanged()
                }
                // Create the AlertDialog
                val alertDialog: android.app.AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                if (recyclerView.adapter != emptyAdapter){
                    alertDialog.show()
                }else{
                    recyclerView.adapter = emptyAdapter
                }
            }
        }
        val touchHelper = ItemTouchHelper(swipeDelete)
        touchHelper.attachToRecyclerView(recyclerView)
    }
    // Change adapter according to favList size
    fun setAdapter(textView: TextView){
        if (favGameList.size == 0){
            textView.setText("Favourites")
            recyclerView.adapter = emptyAdapter
        }else{
            textView.setText("Favourites (${favGameList.size})")
            recyclerView.adapter = adapter
        }
    }
}