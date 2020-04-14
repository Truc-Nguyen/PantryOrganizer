package com.example.pantry_organizer.pantry.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.PantryData
import com.example.pantry_organizer.home.activity.HomeActivity
import kotlinx.android.synthetic.main.fragment_add_custom_food.*
import kotlinx.android.synthetic.main.fragment_add_online_food.*
import kotlinx.android.synthetic.main.fragment_food_list.*
import kotlinx.android.synthetic.main.fragment_login.*

class AddCustomFoodFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_custom_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enter_custom_food_item.setOnClickListener {
            //Call function to add food to pantry food list

            val myToast = Toast.makeText(this.context,"Add Custom Food To Pantry", Toast.LENGTH_SHORT)
            myToast.show()
        }

        custom_return_to_pantry.setOnClickListener {
            val intent = Intent(activity, HomeActivity::class.java)
            activity!!.startActivity(intent)
        }


    }
}

//package com.example.pantry_organizer.pantry.activity
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.view.Menu
//import android.view.MenuItem
//import android.widget.Toast
//import com.example.pantry_organizer.R
//import com.example.pantry_organizer.data.FoodData
//import com.example.pantry_organizer.global.activity.AbstractCameraImageCapture
//import kotlinx.android.synthetic.main.activity_custom_food.*
//
//class AddCustomFoodActivity: AbstractCameraImageCapture() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_custom_food)
//
//        // Support bar attributes.
//        supportActionBar?.title = "Custom Food"
//        supportActionBar?.subtitle = "Back"
//        supportActionBar?.setHomeButtonEnabled(true)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//
//        // Capture a photo to assign to this food.
//        customFood_imageView.setOnClickListener {
//            requestImageCapture()
//        }
//    }
//
//    // Inflate custom food activity app bar menu.
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.create_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//
//
//    // App bar menu button listeners.
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.create_menuItem -> {
//                // todo add fields for required food attributes
//                val name = "test food"
//                val photoImagePath = "blank"
//                // todo sanitize inputs
//                val foodData = FoodData("test food,", null, photoImagePath,0 ,0 ,0 ,0 ,0 ,0)
//
//                // Attempt to push the new pantry to firebase.
//                if (viewModel.addPantry(foodData.getDataMap())) {
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
//                // Return to previous activity.
//                onBackPressed()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    // Result from outbound activity request.
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // Push the URI into firebase storage and publish the cloud filename as photoImagePath.
//        if (requestCode == REQUEST_CAMERA_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
//            pushImage(customFood_imageView)
//        }
//    }
//
//    // Return to previous activity.
//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return true
//    }
//}