package com.example.pantry_organizer.pantry.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.PantryData
import com.example.pantry_organizer.global.activity.AbstractCameraImageCapture
import kotlinx.android.synthetic.main.activity_add_pantry.*

class AddPantryActivity: AbstractCameraImageCapture() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pantry)

        // Support bar attributes.
        supportActionBar?.title = "New Pantry"
        supportActionBar?.subtitle = "Back"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Capture a photo to assign to this food.
        addPantry_pantryImage_imageView.setOnClickListener {
            requestImageCapture()
        }
    }

    // Inflate custom food activity app bar menu.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.done_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // App bar menu button listeners.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.done_menuItem -> {
                // todo add fields for required food attributes
                // todo sanitize inputs
                val pantryData = PantryData(addPantry_pantryName_editText.text.toString(), photoImagePath)

                // Push data to firebase database.
                // todo firebase user data structure? use food name as key? update instead of add?
                db.collection("userData")
                    .document(userID!!)
                    .collection("pantryList")
                    .add(pantryData.getDataMap())

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
            pushImage(addPantry_pantryImage_imageView)
        }
    }

    // Return to previous activity.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}