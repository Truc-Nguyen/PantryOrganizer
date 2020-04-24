package com.example.pantry_organizer.planner.fragment.activity

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.RecipeData
import com.example.pantry_organizer.global.activity.AbstractPantryAppActivity
import com.example.pantry_organizer.planner.fragment.adapter.RecipeSearchAdapter
import kotlinx.android.synthetic.main.activity_mealplan_recipe_search.*
import java.time.LocalDate

class MealplanRecipeSearchActivity: AbstractPantryAppActivity() {
    private var mealplanRecipeSearchList = ArrayList<RecipeData>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mealplan_recipe_search)

        // Extract the extras from intent.
        val mealplanDate = intent.extras!!.getString("MealplanDate")!!

        //convert date to readable view
        val parsedDate = mealplanDate.split(".")
        val date = LocalDate.of(parsedDate[2].toInt(), parsedDate[0].toInt(), parsedDate[1].toInt())
        val weekDay = date.dayOfWeek.toString()
        val correctDate = parsedDate[0] + "/" + parsedDate[1]

        // Support bar attributes.
        supportActionBar?.title = "Add to Pantry"
        supportActionBar?.subtitle = weekDay + " " + correctDate
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        // Set up the recycler view to show api food search.
        val recyclerView = mealplanRecipeSearch_recyclerView
        val adapter = RecipeSearchAdapter(mealplanRecipeSearchList, mealplanDate)
        recyclerView.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(this)

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