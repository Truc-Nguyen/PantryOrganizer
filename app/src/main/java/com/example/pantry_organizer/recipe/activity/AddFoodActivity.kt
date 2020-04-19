package com.example.pantry_organizer.recipe.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.FoodData
import com.example.pantry_organizer.global.activity.AbstractPantryAppActivity
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_add_food.*

class AddFoodActivity: AbstractPantryAppActivity() {
    private lateinit var recipeName: String
    var foodData: FoodData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        // Extract the extras from intent.
        recipeName = intent.extras!!.getString("recipeName")!!
        val query = intent.extras!!.getString("query")!!

        // Support bar attributes.
        supportActionBar?.title = query
        supportActionBar?.subtitle = "Back"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Turn off the button layout until the data has been fetched.
        addFoodDetail_addFoodButton_layout.visibility = View.INVISIBLE
        addFoodDetail_addFoodConfirm_text.text = resources.getString(R.string.add_recipe_food_confirm_button)

        // Request and update the nutritional food data for the query.
        viewModel.getApiFoodNutrition(query)
        viewModel.apiFoodNutritionData.observe(this, Observer { liveData ->
            // Construct food data from api data.
            foodData = FoodData(liveData.foods[0], 1)

            // Construct strings.
            val calories = if (foodData?.calories == null) "N/A" else (foodData?.calories.toString())
            val servingSize = foodData?.getServingSize()
            val fat = if (foodData?.fat == null) "N/A" else (foodData?.fat.toString())
            val sugar = if (foodData?.sugar == null) "N/A" else (foodData?.sugar.toString())
            val carbs = if (foodData?.carbs == null) "N/A" else (foodData?.carbs.toString())
            val protein = if (foodData?.protein == null) "N/A" else (foodData?.protein.toString())

            // Update view object data.
            addFoodDetailCalories_textView.text = calories
            addFoodDetailServingSize_textView.text = servingSize
            addFoodDetailFat_textView.text = fat
            addFoodDetailSugar_textView.text = sugar
            addFoodDetailCarbs_textView.text = carbs
            addFoodDetailProtein_textView.text = protein

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

            // Turn on the button layout.
            addFoodDetail_addFoodButton_layout.visibility = View.VISIBLE

            // Animate the layout into view.
            val animSet = AnimatorSet()
            val y = ObjectAnimator.ofFloat(
                addFoodDetail_addFoodButton_layout,
                "translationY",
                addFoodDetail_addFoodButton_layout.translationY,
                -addFoodDetail_addFoodButton_layout.height.toFloat()
            )
            animSet.play(y)
            animSet.duration = 1000
            animSet.start()

            // Update data layout padding.
            addFoodDetailData_layout.setPadding(0, 0, 0,
                addFoodDetailData_layout.paddingBottom + addFoodDetail_addFoodButton_layout.height)
        })

        // Add to recipe button listener.
        addFoodDetail_addFoodConfirm_button.setOnClickListener {
            // Reset quantity field color.
            addFoodDetailQuantity_editText.background = resources.getDrawable(R.drawable.edit_text_border, null)

            // Sanitize input.
            val input = addFoodDetailQuantity_editText.text.toString()
            if (input == "") {
                Toast.makeText(this, "Please enter a valid quantity.", Toast.LENGTH_LONG).show()
                addFoodDetailQuantity_editText.background = resources.getDrawable(R.drawable.edit_text_border_red, null)
                return@setOnClickListener
            } else if (input.toInt() < 1) {
                Toast.makeText(this, "Quantity to add must be at 1 unit or greater.", Toast.LENGTH_LONG).show()
                addFoodDetailQuantity_editText.background = resources.getDrawable(R.drawable.edit_text_border_red, null)
                return@setOnClickListener
            }

            // Disable the buttons once a request has been made.
            addFoodDetail_addFoodConfirm_button.isEnabled = false
            addFoodDetail_addFoodCancel_button.isEnabled = false

            // Update the food quantity.
            foodData?.quantity = addFoodDetailQuantity_editText.text.toString().toLong()

            // Add the food data to the recipe.
            viewModel.addFoodToRecipe(recipeName, foodData!!)
            Toast.makeText(this, "Added $query to $recipeName!", Toast.LENGTH_LONG).show()

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