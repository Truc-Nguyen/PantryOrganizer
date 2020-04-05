package com.example.pantry_organizer.home.activity

import android.os.Bundle
import com.example.pantry_organizer.R
import com.example.pantry_organizer.global.activity.AbstractPantryAppActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity: AbstractPantryAppActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.title = resources.getString(R.string.pantry_nav)

        // Define navigation menu button listeners.
        appNav_appBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.pantry_navMenu -> {
                    supportActionBar?.title = resources.getString(R.string.pantry_nav)
                }
                R.id.recipe_navMenu -> {
                    supportActionBar?.title = resources.getString(R.string.recipe_nav)
                }
                R.id.planner_navMenu -> {
                    supportActionBar?.title = resources.getString(R.string.planner_nav)
                }
                R.id.shopping_navMenu -> {
                    supportActionBar?.title = resources.getString(R.string.shopping_nav)
                }
            }
            true
        }
    }

    // Prompt user for logout when the back button is pressed.
    override fun onBackPressed() {
        logout()
    }
}