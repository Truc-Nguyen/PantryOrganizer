package com.example.pantry_organizer.recipe.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.FoodData
import com.example.pantry_organizer.global.activity.AbstractPantryAppActivity
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_pantry_food_detail.*

class RecipeFoodDetailActivity: AbstractPantryAppActivity() {
    private lateinit var recipeName: String
    private lateinit var foodName: String
    private var foodIndex: Int = 0
    var foodData: FoodData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantry_food_detail)

        // Extract the extras from intent.
        recipeName = intent.extras!!.getString("recipeName")!!
        foodName = intent.extras!!.getString("foodName")!!
        foodIndex = intent.extras!!.getInt("foodIndex")

        // Support bar attributes.
        supportActionBar?.title = foodName
        supportActionBar?.subtitle = "Back"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Update the nutritional food data.
        viewModel.recipeList.observe(this, Observer { liveData ->
            // Construct food data.
            for (recipe in liveData) {
                if (recipe.name == recipeName) {
                    foodData = recipe.foodList!![foodIndex]
                }
            }

            // Construct strings.
            val quantity = foodData?.quantity.toString()
            val calories = if (foodData?.calories == null) "N/A" else (foodData?.calories.toString())
            val servingSize = foodData?.getServingSize()
            val fat = if (foodData?.fat == null) "N/A" else (foodData?.fat.toString())
            val sugar = if (foodData?.sugar == null) "N/A" else (foodData?.sugar.toString())
            val carbs = if (foodData?.carbs == null) "N/A" else (foodData?.carbs.toString())
            val protein = if (foodData?.protein == null) "N/A" else (foodData?.protein.toString())

            // Update view object data.
            pantryFoodDetailQuantity_textView.text = quantity
            pantryFoodDetailCalories_textView.text = calories
            pantryFoodDetailServingSize_textView.text = servingSize
            pantryFoodDetailFat_textView.text = fat
            pantryFoodDetailSugar_textView.text = sugar
            pantryFoodDetailCarbs_textView.text = carbs
            pantryFoodDetailProtein_textView.text = protein

            // Update image view foodData.
            if (foodData!!.imageLink == null) {
                pantryFoodDetail_imageView.setImageResource(R.drawable.no_image_icon)
            } else {
                Picasso.get()
                    .load(foodData!!.imageLink)
                    .error(R.drawable.no_image_icon)
                    .transform(CropSquareTransformation())
                    .transform(RoundedCornersTransformation(5, 0))
                    .placeholder(R.drawable.loading_icon).into(pantryFoodDetail_imageView)
            }
        })
    }

    // Return to previous activity.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}