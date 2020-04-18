package com.example.pantry_organizer.pantry.activity


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.pantry_organizer.R
import com.example.pantry_organizer.global.activity.AbstractCameraImageCapture
import com.example.pantry_organizer.global.adapter.ViewPagerAdapter
import com.example.pantry_organizer.pantry.fragment.AddCustomFoodFragment
import com.example.pantry_organizer.pantry.fragment.AddOnlineFoodFragment
import kotlinx.android.synthetic.main.activity_add_food.*
import kotlinx.android.synthetic.main.activity_add_pantry.*
import kotlinx.android.synthetic.main.activity_custom_food.*
import kotlinx.android.synthetic.main.fragment_add_custom_food.*


class AddFoodActivity: AbstractCameraImageCapture() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        // Support bar attributes.
        supportActionBar?.title = "New Food"
        supportActionBar?.subtitle = "Back"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    //    get current pantry name
        val currentPantry=intent.getStringExtra("AddFoodToPantry")
        var bundle = Bundle()
        bundle.putString("EnterPantry", currentPantry)
        bundle.putString("PhotoFilename",fbsFilename)
        val customFoodFragment = AddCustomFoodFragment()
        customFoodFragment.arguments = bundle
        val test = AddOnlineFoodFragment()

        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(test, "Search Online")
        viewPagerAdapter.addFragment(customFoodFragment, "Custom Food")

        addFood_viewPager?.adapter = viewPagerAdapter
        addFood_tabLayout.setupWithViewPager(addFood_viewPager)

        // Capture a photo to assign to this food.
//        addFood_customImage_imageView.setOnClickListener {customFoodFragment.view
//            requestImageCapture()
//        }
    }

    // Inflate custom food activity app bar menu.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Return to previous activity.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // Result from outbound activity request.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Push the URI into firebase storage and publish the cloud filename as photoImagePath.
        if (requestCode == REQUEST_CAMERA_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            pushImage(addFood_customImage_imageView)
        }

        //store image fbsFilename and navigate back to
    }

    fun requestImageCaptureNo(view: View){
        requestImageCapture()
    }

}