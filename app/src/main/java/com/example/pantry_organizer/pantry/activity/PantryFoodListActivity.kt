package com.example.pantry_organizer.pantry.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.FoodData
import com.example.pantry_organizer.global.activity.AbstractPantryAppActivity
import com.example.pantry_organizer.pantry.adapter.PantryFoodListAdapter
import kotlinx.android.synthetic.main.activity_pantry_food_list.*

class PantryFoodListActivity: AbstractPantryAppActivity() {
    private lateinit var pantryName: String
    private var pantryIndex = 0
    private var foodList = ArrayList<FoodData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantry_food_list)

        // Extract the extras from intent.
        pantryName = intent.extras!!.getString("pantryName")!!
        pantryIndex = intent.extras!!.getInt("pantryIndex")

        // Support bar attributes.
        supportActionBar?.title = pantryName
        supportActionBar?.subtitle = "Home"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up recycler view to show food list in this pantry.
        val recyclerView = pantry_food_recycler_view
        val adapter = PantryFoodListAdapter(foodList)
        recyclerView.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        // Attach observer to pantry data.
        viewModel.pantryList.observe(this, Observer { liveData ->
            foodList.clear()
            foodList.addAll(liveData[pantryIndex].foodList as Collection<FoodData>)
            adapter.notifyDataSetChanged()
        })
    }

    // Inflate the app menu.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_food_menu, menu)
        return true
    }

    // App menu item listeners.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addFood_menuItem -> {
                val intent = Intent(this, ApiFoodSearchActivity::class.java)
                intent.putExtra("pantryName", pantryName)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Return to previous activity.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}