package com.example.mygames

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mygames.databinding.ActivityMainBinding
import com.example.mygames.fragments.Favourites
import com.example.mygames.fragments.Games
import com.example.mygames.models.GameModel
import kotlin.collections.ArrayList

var favGameList: ArrayList<GameModel> = ArrayList()

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Block adding gameList to tempList more than one
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Start activity with games fragment initially
        replaceFragment(Games())
        // Change the tabs
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.game_nav -> replaceFragment(Games())
                R.id.fav_nav -> replaceFragment(Favourites())
                else -> {
                }
            }
            true
        }
    }
    // Switch between fragments
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentView, fragment)
        fragmentTransaction.commit()
    }
}