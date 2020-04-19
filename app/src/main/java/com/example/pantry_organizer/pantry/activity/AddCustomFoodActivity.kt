package com.example.pantry_organizer.pantry.activity

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

class AddCustomFoodActivity: AbstractPantryAppActivity() {
    private lateinit var pantryName: String
    var foodData: FoodData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        // Extract the extras from intent.
        pantryName = intent.extras!!.getString("pantryName")!!
        val query = intent.extras!!.getString("query")!!

        // Support bar attributes.
        supportActionBar?.title = query
        supportActionBar?.subtitle = "Back"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Turn off the add food button until the data has been fetched.
        addFoodDetail_addFoodConfirm_button.isEnabled = false

        // Request and update the nutritional food data for the query.
        viewModel.getApiFoodNutrition(query)
        viewModel.apiFoodNutritionData.observe(this, Observer { liveData ->
            // Construct food data from api data.
            foodData = FoodData(liveData.foods[0], 3)

            // Update view object data.
            addFoodDetailCalories_textView.text = foodData!!.calories.toString()
            addFoodDetailServingSize_textView.text = foodData!!.getServingSize()
            addFoodDetailFat_textView.text = foodData!!.fat.toString()
            addFoodDetailSugar_textView.text = foodData!!.sugar.toString()
            addFoodDetailCarbs_textView.text = foodData!!.carbs.toString()
            addFoodDetailProtein_textView.text = foodData!!.protein.toString()

            // Update image view foodData.
            if (foodData!!.imageLink == null) {
                addFoodDetail_imageView.setImageResource(R.drawable.no_image_icon)
            } else {
                Picasso.get()
                    .load(foodData!!.imageLink)
                    .error(R.drawable.no_image_icon)
                    .transform(CropSquareTransformation())
                    .transform(RoundedCornersTransformation(5, 0))
                    .placeholder(R.drawable.loading_icon).into(addFoodDetail_imageView)
            }

            // Enable the add food button.
            addFoodDetail_addFoodConfirm_button.isEnabled = true
        })

        // Add to pantry button listener.
        addFoodDetail_addFoodConfirm_button.setOnClickListener {
            // Add the food data to the pantry.
            viewModel.addFoodToPantry(pantryName, foodData!!)
            Toast.makeText(this, "Added $query to $pantryName!", Toast.LENGTH_LONG).show()

            // Return to food search list.
            onBackPressed()
        }

        // Cancel button listener.
        addFoodDetail_addFoodCancel_button.setOnClickListener {
            // Return to food search list.
            onBackPressed()
        }
    }

    // Return to previous activity.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}