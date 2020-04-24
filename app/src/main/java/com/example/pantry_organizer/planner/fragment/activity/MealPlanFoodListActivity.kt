package com.example.pantry_organizer.planner.fragment.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.FoodData
import com.example.pantry_organizer.global.activity.AbstractPantryAppActivity
import com.example.pantry_organizer.recipe.activity.RecipeImageZoomActivity
import com.example.pantry_organizer.recipe.adapter.RecipeFoodListAdapter
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_mealplan_food_list.*

class MealPlanFoodListActivity: AbstractPantryAppActivity() {
    private lateinit var recipeName: String
    private var recipeIndex = 0
    private var foodList = ArrayList<FoodData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mealplan_food_list)

        // Extract the extras from intent.
        recipeName = intent.extras!!.getString("recipeName")!!
        recipeIndex = intent.extras!!.getInt("recipeIndex")

        // Support bar attributes.
        supportActionBar?.title = recipeName
        supportActionBar?.subtitle = "Home"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up recycler view to show food list in this recipe.
        val recyclerView = mealPlan_food_recycler_view
        val adapter = RecipeFoodListAdapter(foodList, recipeName)
        recyclerView.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        // Attach observer to recipe data.
        viewModel.recipeList.observe(this, Observer { liveData ->
            // Set up recipe details.
            val recipeData = liveData[recipeIndex]
            mealPlanFoodList_serving_textView.text = "${recipeData.getFoodCalories()} Calories"
            mealPlanFoodList_quantity_textView.text =
                "${recipeData?.getFoodTypeCount()} Food Types / ${recipeData.getFoodTotalCount()} Total"
            mealPlanFoodList_rating_ratingBar.rating = recipeData.rating.toFloat()
            mealPlanFoodList_rating_ratingBar.setIsIndicator(true)
            if (recipeData.imageLink == null) {
                mealPlanFoodList_recipe_imageView.setImageResource(R.drawable.no_image_icon)
            } else {
                val imageRef = Firebase.storage.reference.child(recipeData.imageLink)
                imageRef.downloadUrl.addOnSuccessListener {
                    Picasso.get()
                        .load(it)
                        .transform(CropSquareTransformation())
                        .transform(RoundedCornersTransformation(25, 0))
                        .placeholder(R.drawable.loading_icon).into(mealPlanFoodList_recipe_imageView)

                    //set up onclick listener for taking user to zoomed version of the recipe
                    mealPlanFoodList_recipe_imageView.setOnClickListener{
                        val intent = Intent(this, RecipeImageZoomActivity::class.java)
                        intent.putExtra("recipeImgLink", recipeData.imageLink)
                        intent.putExtra("recipeName", recipeData.name)
                        startActivity(intent)
                    }
                }.addOnFailureListener {
                    mealPlanFoodList_recipe_imageView.setImageResource(R.drawable.no_image_icon)
                }
            }

            // Set up food list details.
            if (recipeData.foodList!!.isEmpty()) {
                mealPlanFoodNoItems_textView.visibility = View.VISIBLE
                mealPlanFood_foodLabel_textView.visibility = View.INVISIBLE
            } else {
                mealPlanFoodNoItems_textView.visibility = View.INVISIBLE
                mealPlanFood_foodLabel_textView.visibility = View.VISIBLE
            }

            foodList.clear()
            foodList.addAll(recipeData.foodList as Collection<FoodData>)
            adapter.notifyDataSetChanged()
        })
    }

    // Return to previous activity.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}