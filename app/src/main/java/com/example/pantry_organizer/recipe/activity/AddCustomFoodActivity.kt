package com.example.pantry_organizer.recipe.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.FoodData
import com.example.pantry_organizer.global.activity.AbstractCameraImageCapture
import com.example.pantry_organizer.global.activity.AbstractPantryAppActivity
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_add_custom_food.*
import kotlinx.android.synthetic.main.activity_add_food.*

class AddCustomFoodActivity: AbstractCameraImageCapture() {
    private lateinit var recipeName: String
    var foodData: FoodData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_custom_food)

        // Extract the extras from intent.
        recipeName = intent.extras!!.getString("recipeName")!!

        // Support bar attributes.
        supportActionBar?.title = "Create Custom Food"
        supportActionBar?.subtitle = recipeName
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Capture a photo to assign to this food.
        addCustomFood_image_imageView.setOnClickListener {
            requestImageCapture()

        }

        // Add to recipe button listener.
        addCustomFood_addConfirm_button.setOnClickListener {
            // Harvest user input.
            val name = addCustomFood_name_editText.text.toString()
            val quantity = addCustomFood_quantity_editText.text.toString()
            val calories = addCustomFood_calories_editText.text.toString()
            val servingQty = addCustomFood_servingQty_editText.text.toString()
            val servingUnit = addCustomFood_servingUnit_editText.text.toString()
            val fat = addCustomFood_fat_editText.text.toString()
            val sugar = addCustomFood_sugar_editText.text.toString()
            val carbs = addCustomFood_carbs_editText.text.toString()
            val protein = addCustomFood_protein_editText.text.toString()

            // Sanitize input.
            if (name == "") {
                Toast.makeText(this, "Food name cannot be blank.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else if (quantity == "") {
                Toast.makeText(this, "Quantity cannot be blank.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else if (quantity.toInt() < 1) {
                Toast.makeText(this, "Quantity must be greater than zero.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Construct the food data object.
            val foodData = FoodData(
                name,
                null,
                fbsFilename,
                if (quantity == "") 0L else (quantity.toLong()),
                if (calories == "") 0.0 else (calories.toDouble()),
                if (servingQty == "") "1" else (servingQty),
                if (servingUnit == "") "count" else (servingUnit),
                if (fat == "") 0.0 else (fat.toDouble()),
                if (sugar == "") 0.0 else (sugar.toDouble()),
                if (carbs == "") 0.0 else (carbs.toDouble()),
                if (protein == "") 0.0 else (protein.toDouble())
            )

            // Add the food data to the recipe.
            viewModel.addFoodToRecipe(recipeName, foodData)
            Toast.makeText(this, "Added $name to $recipeName!", Toast.LENGTH_LONG).show()

            // Return to food search list.
            onBackPressed()
        }

        // Cancel button listener.
        addCustomFood_addCancel_button.setOnClickListener {
            // Return to food search list.
            onBackPressed()
        }
    }

    // Result from outbound activity request.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Push the URI into firebase storage and publish the cloud filename as photoImagePath.
        if (requestCode == REQUEST_CAMERA_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            pushImage(addCustomFood_image_imageView)
        }
    }

    // Return to previous activity.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}