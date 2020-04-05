package com.example.pantry_organizer.pantry.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.FoodData
import com.example.pantry_organizer.global.activity.AbstractPantryAppActivity
import kotlinx.android.synthetic.main.activity_custom_food.*
import java.io.ByteArrayOutputStream
import java.util.*

class CustomFoodActivity: AbstractPantryAppActivity() {
    // Activity request codes.
    private val REQUEST_CAMERA_PERMISSIONS = 1
    private val REQUEST_IMAGE_CAPTURE = 2

    private var customImage: Bitmap? = null

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
            val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSIONS)
            } else {
                val cIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cIntent, REQUEST_IMAGE_CAPTURE)
            }
        }

        // todo this is reference code for pulling a picture into firebase storage
//        val imageRef = fbs.child("firebase image path here")
//
//        imageRef.downloadUrl.addOnSuccessListener {
//            Picasso.get()
//                .load(it)
//                .transform(CropSquareTransformation())
//                .transform(RoundedCornersTransformation(0, 0))
//                .placeholder(R.drawable.loading_icon).into(customFood_imageView)
//        }.addOnFailureListener {
//            customFood_imageView.setImageResource(R.drawable.no_image_icon)
//        }
    }

    // Inflate custom food activity app bar menu.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_custom_food_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // App bar menu button listeners.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addCustomFood_menuItem -> {
                // todo add fields for required food attributes
                // todo sanitize inputs
                val foodData = FoodData("test food,", null, null)
                if (customImage != null) {
                    // Condition the captured image into a byte array.
                    val outputStream = ByteArrayOutputStream()
                    val bitmap = customImage!!
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    val byteArray = outputStream.toByteArray()

                    // Generate unique filename.
                    val filename = "${UUID.randomUUID()}.jpg"

                    // Update image link in food data object.
                    foodData.imageLink = filename

                    // Push image to firebase storage.
                    val upload = fbs.child(filename)
                    upload.putBytes(byteArray)
                }

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

    // Result from activity request.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Take a picture of custom food and set it as an image view bitmap.
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val bitmap = data!!.extras!!["data"] as Bitmap
            customFood_imageView.setImageBitmap(bitmap)
            customImage = bitmap
        }
    }

    // Return to previous activity.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}