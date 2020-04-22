package com.example.pantry_organizer.planner.fragment.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.FoodData
import com.example.pantry_organizer.data.MealplanData
import com.example.pantry_organizer.data.RecipeData
import com.example.pantry_organizer.global.activity.AbstractPantryAppActivity
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_add_food.*
import kotlinx.android.synthetic.main.activity_add_mealplan_recipe.*

class AddMealplanRecipeActivity: AbstractPantryAppActivity() {
    private lateinit var recipeName: String
    private lateinit var mealplanDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_mealplan_recipe)

        // Extract the extras from intent.
        recipeName = intent.extras!!.getString("recipeName")!!
        mealplanDate = intent.extras!!.getString("mealplanDate")!!


        // Support bar attributes.
        supportActionBar?.title = recipeName
        supportActionBar?.subtitle = "Back"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Turn off the add food button until the data has been fetched.
        addRecipeDetail_addRecipeConfirm_button.isEnabled = false

            addRecipeDetail_addRecipeConfirm_button.isEnabled = true
//        })

        // Add to pantry button listener.
        addRecipeDetail_addRecipeConfirm_button.setOnClickListener {

            val mealPlanData = MealplanData(mealplanDate, emptyList())

            //todo: check if mealplan data object exists in firebase
            // Attempt to push the new recipe to firebase.
            //date not yet in firebase
            viewModel.getDates()

            viewModel.dateList.observe(this, Observer {
                var test = false
                val test1 = viewModel.dateList.value
                for (i in 0 until viewModel.dateList.value!!.size){
                    if(test1!![i].date == mealplanDate) {
                        test = true
                    }
                }

                if(test){
                    Log.d("addmealplan","date already existed")
                    viewModel.addRecipeToDate(mealplanDate,recipeName)
//                    // Push successful.
//                    Toast.makeText(this, "First recipe added to $mealplanDate.", Toast.LENGTH_SHORT).show()
                }else{
                    Log.d("addmealplan","date didnt existed")
                    viewModel.addDate(mealPlanData.getDataMap())
                    viewModel.dateList.observe(this, Observer{
                        viewModel.addRecipeToDate(mealplanDate,recipeName)
                    })
                }
                Toast.makeText(this, "Recipe added to $mealplanDate.", Toast.LENGTH_SHORT).show()

//                Log.d("datelist", "changed")
//                if (viewModel.addDate(mealPlanData.getDataMap())) {
//                    Log.d("addmealplan","addDate succeeded")
//                    viewModel.addRecipeToDate(mealplanDate,recipeName)
//                    // Push successful.
//                    Toast.makeText(this, "First recipe added to $mealplanDate.", Toast.LENGTH_LONG).show()
//                }
                //date is already in firebase
//                else {
//                    viewModel.addRecipeToDate(mealplanDate,recipeName)
//                    Toast.makeText(this, "$recipeName added to $mealplanDate.", Toast.LENGTH_LONG).show()
//                }
            })


            // Return to previous activity.
            onBackPressed()
        }

        // Cancel button listener.
        addRecipeDetail_addRecipeCancel_button.setOnClickListener {
            // Return to recipe search list.
            onBackPressed()
        }
    }

    // Return to previous activity.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}