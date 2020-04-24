package com.example.pantry_organizer.recipe.activity

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.FoodData
import com.example.pantry_organizer.global.activity.AbstractPantryAppActivity
import com.example.pantry_organizer.global.adapter.SwipeController
import com.example.pantry_organizer.global.adapter.SwipeControllerActions
import com.example.pantry_organizer.recipe.adapter.RecipeFoodListAdapter
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_recipe_food_list.*
import kotlinx.android.synthetic.main.activity_recipe_zoom.*
import kotlinx.android.synthetic.main.dialog_confirm_remove_food.*

class RecipeImageZoomActivity: AbstractPantryAppActivity() {
    private lateinit var recipeName: String
    private lateinit var recipeImgLink: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_zoom)

        // Extract the extras from intent.
        recipeImgLink = intent.extras!!.getString("recipeImgLink")!!
        recipeName = intent.extras!!.getString("recipeName")!!


        // Support bar attributes.
        supportActionBar?.title = recipeName
        supportActionBar?.subtitle = "Home"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imageRef = Firebase.storage.reference.child(recipeImgLink)
        imageRef.downloadUrl.addOnSuccessListener {
            Picasso.get()
                .load(it)
//                .transform(CropSquareTransformation())
//                .transform(RoundedCornersTransformation(25, 0))
                .placeholder(R.drawable.loading_icon).into(recipe_zoom_imageView)
        }.addOnFailureListener {
            recipe_zoom_imageView.setImageResource(R.drawable.no_image_icon)
        }
    }


    // Inflate the app menu.
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.add_food_menu, menu)
//        return true
//    }

    // App menu item listeners.
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.addFood_menuItem -> {
//                val intent = Intent(this, ApiFoodSearchActivity::class.java)
//                intent.putExtra("recipeName", recipeName)
//                startActivity(intent)
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    // Return to previous activity.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

//    override fun onBackPressed() {
////        val rating = recipeFoodList_rating_ratingBar.rating
////        viewModel.updateRecipeRating(recipeName, rating)
//        super.onBackPressed()
//    }
}