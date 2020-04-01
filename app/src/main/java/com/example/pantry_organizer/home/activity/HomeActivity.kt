package com.example.pantry_organizer.home.activity

import android.os.Bundle
import com.example.pantry_organizer.R
import com.example.pantry_organizer.global.activity.AbstractPantryAppActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity: AbstractPantryAppActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    override fun onStart() {
        super.onStart()

        // Define navigation menu button listeners.
        appNav_appBar.setOnNavigationItemReselectedListener { item ->
            when (item.itemId) {
                R.id.pantry_navMenu -> {

                }
                R.id.recipe_navMenu -> {

                }
                R.id.planner_navMenu -> {

                }
                R.id.shopping_navMenu -> {

                }
            }
        }
    }
}