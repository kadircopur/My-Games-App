package com.example.mygames.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mygames.R
import com.example.mygames.adapters.EmptyPageAdapter
import com.example.mygames.adapters.GameAdapter
import com.example.mygames.adapters.PaginationScrollListener
import com.example.mygames.models.GameModel
import com.example.mygames.models.ResponseModel
import com.example.mygames.services.GamesApi
import com.example.mygames.services.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Games.newInstance] factory method to
 * create an instance of this fragment.
 */
class Games : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private var isLoading = false
    private var isLastPage = false
    private lateinit var adapter: GameAdapter
    private lateinit var emptyAdapter: EmptyPageAdapter
    private lateinit var searchView: android.widget.SearchView
    private var tempGameList: ArrayList<GameModel> = ArrayList()
    private var resultList: MutableList<GameModel> = mutableListOf()
    private var searchList: MutableList<GameModel> = mutableListOf()
    private lateinit var apiService: GamesApi
    private val PAGE_START = 1
    private val TOTAL_PAGES = 20
    private var currentPage = PAGE_START
    private val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

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
        return inflater.inflate(R.layout.fragment_games, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Games.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Games().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val emptyMessage: ArrayList<String> = ArrayList<String>()
        emptyMessage.add("No game has been searched")
        searchView = view.findViewById(R.id.search_bar)
        recyclerView = view.findViewById(R.id.gameRecyc)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
        emptyAdapter = EmptyPageAdapter(requireContext(), emptyMessage)
        adapter = GameAdapter(requireContext(), tempGameList)
        recyclerView.adapter = adapter
        apiService = RetrofitInstance.apiService.create(GamesApi::class.java)
        addScrollListener()
        initListeners(adapter)
        loadFirstPage()
    }

    private fun addScrollListener(){
        recyclerView.addOnScrollListener(object: PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1
                loadNextPage()}

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })
    }

    private fun loadNextPage() {
        apiService.getGames(10, currentPage).enqueue(object: Callback<ResponseModel>{
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.isSuccessful){
                    resultList.addAll(response.body()!!.results.toMutableList())
                    tempGameList.addAll(response.body()!!.results.toMutableList())
                    isLoading = false
                    adapter.notifyDataSetChanged()

                    if (currentPage == TOTAL_PAGES){
                        isLastPage = true
                    }
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) { t.printStackTrace() }
        })
    }


    private fun loadFirstPage() {
        apiService.getGames(10, 1).enqueue(object: Callback<ResponseModel>{
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.isSuccessful){
                    resultList.addAll(response.body()!!.results.toMutableList())
                    tempGameList.addAll(response.body()!!.results.toMutableList())
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) { t.printStackTrace() }
        })
    }

    private fun initListeners(adapter: GameAdapter) {
        searchView.setOnQueryTextListener(object: android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchText = query!!.toLowerCase(Locale.getDefault())
                if (searchText.length >= 3){
                    apiService.getGameSearch(searchText).enqueue(object: Callback<ResponseModel>{
                            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                                if (response.isSuccessful){
                                    recyclerView.clearOnScrollListeners()
                                    tempGameList.clear()
                                    tempGameList.addAll(response.body()!!.results.toMutableList())
                                    recyclerView.adapter = adapter
                                    adapter.notifyDataSetChanged()
                                }
                            }
                            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                                t.printStackTrace() }
                        }
                    )
                    if (tempGameList.isEmpty()){
                        recyclerView.adapter = emptyAdapter
                    }
                }else{
                    tempGameList.clear()
                    tempGameList.addAll(resultList)
                    adapter.notifyDataSetChanged()
                }
                return false }

            override fun onQueryTextChange(newText: String?): Boolean {
                tempGameList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                recyclerView.adapter = adapter
                if (searchText.length >= 3){
                    recyclerView.clearOnScrollListeners()
                    resultList.forEach {
                        if (it.name.toLowerCase().contains(searchText)){
                            tempGameList.add(it)
                        }
                    }
                    if (tempGameList.isEmpty()){
                        recyclerView.adapter = emptyAdapter
                    }

                    adapter.notifyDataSetChanged()
                }else{
                    addScrollListener()
                    tempGameList.clear()
                    tempGameList.addAll(resultList)
                    adapter.notifyDataSetChanged()
                }
                return false
            }
        })
    }

}