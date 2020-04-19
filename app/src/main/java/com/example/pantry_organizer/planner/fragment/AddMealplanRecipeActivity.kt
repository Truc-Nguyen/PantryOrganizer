package com.example.pantry_organizer.planner.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.FoodData
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

        // Request and update the nutritional food data for the query.
       // viewModel.getApiFoodNutrition(query)
//        viewModel.getingleRecipe(recipeName)
//        viewModel.apiFoodNutritionData.observe(this, Observer { liveData ->
//            // Construct food data from api data.
//            foodData = FoodData(liveData.foods[0])
//
//            // Update view object data.
//            addFoodDetailCalories_textView.text = foodData!!.calories.toString()
//            addFoodDetailServingSize_textView.text = "${foodData!!.servingQty} ${foodData!!.servingUnit}"
//            addFoodDetailFat_textView.text = foodData!!.fat.toString()
//            addFoodDetailSugar_textView.text = foodData!!.sugar.toString()
//            addFoodDetailCarbs_textView.text = foodData!!.carbs.toString()
//            addFoodDetailProtein_textView.text = foodData!!.protein.toString()
//
//            // Update image view foodData.
//            if (foodData!!.imageLink == null) {
//                addFoodDetail_imageView.setImageResource(R.drawable.no_image_icon)
//            } else {
//                Picasso.get()
//                    .load(foodData!!.imageLink)
//                    .error(R.drawable.no_image_icon)
//                    .transform(CropSquareTransformation())
//                    .transform(RoundedCornersTransformation(5, 0))
//                    .placeholder(R.drawable.loading_icon).into(addFoodDetail_imageView)
//            }

            // Enable the add food button.
            addRecipeDetail_addRecipeConfirm_button.isEnabled = true
//        })

        // Add to pantry button listener.
        addRecipeDetail_addRecipeConfirm_button.setOnClickListener {
            // Add the recipe data to list of strings for a mealplanData
//            viewModel.addFood(pantryName, foodData!!.getDataMap())
            Toast.makeText(this, "Added $recipeName to $mealplanDate!", Toast.LENGTH_LONG).show()

            // Return to food search list.
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