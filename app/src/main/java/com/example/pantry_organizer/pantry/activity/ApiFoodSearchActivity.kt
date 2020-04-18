package com.example.pantry_organizer.pantry.activity

import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.ApiFoodData
import com.example.pantry_organizer.global.activity.AbstractPantryAppActivity
import com.example.pantry_organizer.pantry.adapter.ApiSearchAdapter
import kotlinx.android.synthetic.main.activity_api_food_search.*

class ApiFoodSearchActivity: AbstractPantryAppActivity() {
    private lateinit var pantryName: String
    private var apiSearchList = ArrayList<ApiFoodData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_food_search)

        // Extract the extras from intent.
        pantryName = intent.extras!!.getString("pantryName")!!

        // Support bar attributes.
        supportActionBar?.title = "Add to Pantry"
        supportActionBar?.subtitle = pantryName
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up the recycler view to show api food search.
        val recyclerView = apiFoodSearch_recyclerView
        val adapter = ApiSearchAdapter(apiSearchList, pantryName)
        recyclerView.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        // Attach observer to api search data.
        viewModel.apiSearchList.observe(this, Observer { liveData ->
            apiSearchList.clear()
            apiSearchList.addAll(liveData.common)
            adapter.notifyDataSetChanged()
        })

        // Attach click listener to search button.
        apiFoodSearch_button.setOnClickListener {
            viewModel.getApiSearchList(apiFoodSearch_editText.text.toString())
            recyclerView.scrollToPosition(0)
        }
    }

    // Inflate custom food activity app bar menu.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_custom_food_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Return to previous activity.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}