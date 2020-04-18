package com.example.pantry_organizer.pantry.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.FireBaseFood
//import com.example.pantry_organizer.data.Photo
import com.example.pantry_organizer.global.viewModel.AppViewModel
import kotlinx.android.synthetic.main.fragment_add_custom_food.*

class AddCustomFoodFragment: Fragment() {

    lateinit var viewModel: AppViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //enable menu functions
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_custom_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
//        custom_food_picture_button.setOnClickListener {
//            val intent = Intent(activity, AddCustomFoodCameraActivity::class.java)
//            activity!!.startActivity(intent)
//        }

    }

    // App bar menu button listeners.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.create_menuItem -> {
                Log.d("addcustomfood","create menu button pressed")

                // Harvest user input.
                val name = enter_food_name.text.toString()
                val amount = enter_food_amount.text.toString()
                // Sanitize input for necessary fields
                if (name == "") {
                    Toast.makeText(this.context, "Food name cannot be blank.", Toast.LENGTH_LONG).show()
                    return true
                } else if (amount == "") {
                    Toast.makeText(this.context, "Food amount cannot be blank.", Toast.LENGTH_LONG).show()
                    return true
                } else if (name.contains(",") || amount.contains(",")){
                    Toast.makeText(this.context, "Name and Amount fields cannot contain commas.", Toast.LENGTH_LONG).show()
                    return true
                }

                //optional entries
                var calories = 0.0
                if (enter_food_calories.text.toString() != "") calories = enter_food_calories.text.toString().toDouble()
                var servingSize = "Not known"
                if (enter_food_serving_size_quantity.text.toString() != "") servingSize = enter_food_serving_size_quantity.text.toString()
                var servingUnit = "Not known"
                if (enter_food_serving_size_unit.text.toString() != "") servingSize = enter_food_serving_size_unit.text.toString()
                var fat = 0.0
                if (enter_food_fat.text.toString() != "") fat = enter_food_fat.text.toString().toDouble()
                var carbs = 0.0
                if (enter_food_carbs.text.toString() != "") carbs = enter_food_carbs.text.toString().toDouble()
                var sugar = 0.0
                if (enter_food_sugar.text.toString() != "") sugar = enter_food_sugar.text.toString().toDouble()
                var protein = 0.0
                if (enter_food_protein.text.toString() != "") protein = enter_food_protein.text.toString().toDouble()

                //get current pantry location
                //retrieve arguments from previous fragment
                val bundle = this.arguments
                val currentPantry = bundle!!.getString("EnterPantry","none")
                val photoFilename = bundle!!.getString("PhotoFilename","")
                Log.d("addcustomfood","current pantry: $currentPantry")
                Log.d("addcustomfood","name: $name")
                Log.d("addcustomfood","amount: $amount")
                val foodNameAndAmount: String = "$name,$amount"
                Log.d("addcustomfood","added to pantry foodlist")

                // Create new food database entry
               val foodData = FireBaseFood(
                   servingSize,
                   servingUnit,
                   calories,
                   fat,
                   carbs,
                   sugar,
                   protein,
                   photoFilename,
                   name
                   )
                // Attempt to push the new pantry to firebase.
                if (viewModel.addFoodToFirebase(foodData.getDataMap())) {
                    // Push successful.
                    Toast.makeText(this.context, "$name added to firebase", Toast.LENGTH_LONG).show()
//                    if (viewModel.addFoodToPantry(currentPantry,foodNameAndAmount)){
//                        Toast.makeText(this.context, "$name added to pantry food list", Toast.LENGTH_LONG).show()
//                        // Return to previous activity.
//                        this.activity!!.onBackPressed()
//                    }
//                } else {
                    // Pantry with this name already exists.
                    Toast.makeText(this.context, "$name already exists.", Toast.LENGTH_LONG).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
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