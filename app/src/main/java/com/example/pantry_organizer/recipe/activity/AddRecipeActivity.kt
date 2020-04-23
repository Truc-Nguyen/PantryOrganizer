package com.example.pantry_organizer.recipe.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.RecipeData
import com.example.pantry_organizer.global.activity.AbstractCameraImageCapture
import kotlinx.android.synthetic.main.activity_add_recipe.*

class AddRecipeActivity: AbstractCameraImageCapture() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        // Support bar attributes.
        supportActionBar?.title = "New Recipe"
        supportActionBar?.subtitle = "Back"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Capture a photo to assign to this food.
        addRecipe_recipeImage_imageView.setOnClickListener {
            requestImageCapture()
        }
    }

    // Inflate custom food activity app bar menu.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // App bar menu button listeners.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.create_menuItem -> {
                addRecipe_recipeName_editText.backgroundTintList = resources.getColorStateList(R.color.darkGray, null)

                // Harvest user input.
                val name = addRecipe_recipeName_editText.text.toString()

                // Sanitize input.
                if (name == "") {
                    addRecipe_recipeName_editText.backgroundTintList = resources.getColorStateList(R.color.red, null)
                    Toast.makeText(this, "Recipe name cannot be blank.", Toast.LENGTH_LONG).show()
                    return true
                }

                // Create new recipe data entry.
                val recipeData = RecipeData(name, fbsFilename, null,0.0, null)

                // Attempt to push the new recipe to firebase.
                if (viewModel.addRecipe(recipeData.getDataMap())) {
                    // Push successful.
                    Toast.makeText(this, "$name added to pantries.", Toast.LENGTH_LONG).show()

                    // Return to previous activity.
                    onBackPressed()
                } else {
                    // Recipe with this name already exists.
                    addRecipe_recipeName_editText.backgroundTintList = resources.getColorStateList(R.color.red, null)
                    Toast.makeText(this, "$name already exists.", Toast.LENGTH_LONG).show()
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Result from outbound activity request.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Push the URI into firebase storage and publish the cloud filename as photoImagePath.
        if (requestCode == REQUEST_CAMERA_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            pushImage(addRecipe_recipeImage_imageView)
        }
    }

    // Return to previous activity.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}