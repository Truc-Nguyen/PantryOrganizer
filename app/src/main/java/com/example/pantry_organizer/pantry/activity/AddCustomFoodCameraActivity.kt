package com.example.pantry_organizer.pantry.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.PantryData
import com.example.pantry_organizer.global.activity.AbstractCameraImageCapture
import kotlinx.android.synthetic.main.activity_add_pantry.*
import kotlinx.android.synthetic.main.activity_add_custom_food.*

class AddCustomFoodCameraActivity: AbstractCameraImageCapture() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pantry)

        // Support bar attributes.
        supportActionBar?.title = "New Food"
        supportActionBar?.subtitle = "Back"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Capture a photo to assign to this food.
        addPantry_pantryImage_imageView.setOnClickListener {
            requestImageCapture()

        }
    }

    // Inflate custom food activity app bar menu.
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.create_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

    // App bar menu button listeners.
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.create_menuItem -> {
//                // Harvest user input.
//                val name = addPantry_pantryName_editText.text.toString()
//                val location = addPantry_pantryLocation_editText.text.toString()
//
//                // Sanitize input.
//                if (name == "") {
//                    Toast.makeText(this, "Pantry name cannot be blank.", Toast.LENGTH_LONG).show()
//                    return true
//                } else if (location == "") {
//                    Toast.makeText(this, "Pantry location cannot be blank.", Toast.LENGTH_LONG).show()
//                    return true
//                }
//
//                // Create new pantry data entry.
////                val pantryData = PantryData(name, location, fbsFilename)
//                val newFoodList : List<String> = emptyList()
//                val pantryData = PantryData(name, location, fbsFilename, newFoodList)
//
//                // Attempt to push the new pantry to firebase.
//                if (viewModel.addPantry(pantryData.getDataMap())) {
//                    // Push successful.
//                    Toast.makeText(this, "$name added to pantries.", Toast.LENGTH_LONG).show()
//
//                    // Return to previous activity.
//                    onBackPressed()
//                } else {
//                    // Pantry with this name already exists.
//                    Toast.makeText(this, "$name already exists.", Toast.LENGTH_LONG).show()
//                }
//
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    // Result from outbound activity request.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Push the URI into firebase storage and publish the cloud filename as photoImagePath.
        if (requestCode == REQUEST_CAMERA_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            pushImage(addFood_customImage_imageView)
        }

        //store image fbsFilename and navigate back to
    }

    // Return to previous activity.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}