package com.example.pantry_organizer.planner.fragment

import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.ApiFoodData
import com.example.pantry_organizer.data.RecipeData
import com.example.pantry_organizer.global.activity.AbstractPantryAppActivity
import com.example.pantry_organizer.pantry.adapter.ApiSearchAdapter
import kotlinx.android.synthetic.main.activity_api_food_search.*
import kotlinx.android.synthetic.main.activity_mealplan_recipe_search.*

class MealplanRecipeSearchActivity: AbstractPantryAppActivity() {
    private lateinit var pantryName: String
    private var mealplanRecipeSearchList = ArrayList<RecipeData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mealplan_recipe_search)

        // Extract the extras from intent.
        val date = intent.extras!!.getString("MealplanDate")!!

        // Support bar attributes.
        supportActionBar?.title = "Add to Pantry"
        supportActionBar?.subtitle = pantryName
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up the recycler view to show api food search.
        val recyclerView = mealplanRecipeSearch_recyclerView
        val adapter = RecipeSearchAdapter(mealplanRecipeSearchList, date)
        recyclerView.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        // todo: Attach observer to api search data.
        viewModel.recipeList.observe(this, Observer { liveData ->
            mealplanRecipeSearchList.clear()
            mealplanRecipeSearchList.addAll(liveData)
            adapter.notifyDataSetChanged()
        })
    }

    // Return to previous activity.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}