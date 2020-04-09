package com.example.pantry_organizer.pantry.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.FoodData
import com.example.pantry_organizer.global.activity.AbstractCameraImageCapture
import kotlinx.android.synthetic.main.activity_custom_food.*

class AddCustomFoodActivity: AbstractCameraImageCapture() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_food)

        // Support bar attributes.
        supportActionBar?.title = "Custom Food"
        supportActionBar?.subtitle = "Back"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Capture a photo to assign to this food.
        customFood_imageView.setOnClickListener {
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
                // todo add fields for required food attributes
                // todo sanitize inputs
                val foodData = FoodData("test food,", null, photoImagePath)

                // Push data to firebase database.
                // todo firebase user data structure? use food name as key? update instead of add?
                db.collection(userID!!)
                    .add(foodData.getDataMap())

                // Return to previous activity.
                onBackPressed()
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
            pushImage(customFood_imageView)
        }
    }

    // Return to previous activity.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}