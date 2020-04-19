package com.example.pantry_organizer.recipe.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.ApiFoodData
import com.example.pantry_organizer.global.activity.AbstractPantryAppActivity
import com.example.pantry_organizer.recipe.adapter.ApiSearchAdapter
import kotlinx.android.synthetic.main.activity_api_food_search.*

class ApiFoodSearchActivity: AbstractPantryAppActivity() {
    private lateinit var recipeName: String
    private var apiSearchList = ArrayList<ApiFoodData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_food_search)

        // Extract the extras from intent.
        recipeName = intent.extras!!.getString("recipeName")!!

        // Support bar attributes.
        supportActionBar?.title = "Add to Recipe"
        supportActionBar?.subtitle = recipeName
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up the recycler view to show api food search.
        val recyclerView = apiFoodSearch_recyclerView
        val adapter = ApiSearchAdapter(apiSearchList, recipeName)
        recyclerView.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        // Attach observer to api search data.
        viewModel.apiSearchList.observe(this, Observer { liveData ->
            apiSearchList.clear()
            apiSearchList.addAll(liveData.common)
            adapter.notifyDataSetChanged()

            // Enable search button.
            apiFoodSearch_button.backgroundTintList = resources.getColorStateList(R.color.green, null)
            apiFoodSearch_button.text = resources.getString(R.string.api_food_search_button)
            apiFoodSearch_button.isEnabled = true
        })

        // Attach click listener to search button.
        apiFoodSearch_button.setOnClickListener {
            // Disable search button until query is complete.
            apiFoodSearch_button.backgroundTintList = resources.getColorStateList(R.color.darkGray, null)
            apiFoodSearch_button.text = resources.getString(R.string.api_food_searching_button)
            apiFoodSearch_button.isEnabled = false

            // Request the query search.
            viewModel.getApiSearchList(apiFoodSearch_editText.text.toString())
            recyclerView.scrollToPosition(0)
        }
    }

    // Inflate custom food activity app bar menu.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_custom_food_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // App menu item listeners.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_custom_menuItem -> {
                val intent = Intent(this, AddCustomFoodActivity::class.java)
                intent.putExtra("recipeName", recipeName)
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