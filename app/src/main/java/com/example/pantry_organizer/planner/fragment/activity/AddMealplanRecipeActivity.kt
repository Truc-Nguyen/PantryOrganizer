package com.example.pantry_organizer.planner.fragment.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.FoodData
import com.example.pantry_organizer.data.MealplanData
import com.example.pantry_organizer.data.RecipeData
import com.example.pantry_organizer.data.ShoppingData
import com.example.pantry_organizer.global.activity.AbstractPantryAppActivity
import com.example.pantry_organizer.recipe.adapter.RecipeFoodListAdapter
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_add_food.*
import kotlinx.android.synthetic.main.activity_add_mealplan_recipe.*
import kotlinx.android.synthetic.main.activity_recipe_food_list.*
import kotlinx.android.synthetic.main.dialog_add_ingredient_to_shopping.*
import kotlinx.android.synthetic.main.dialog_add_item_to_shopping.*
import org.w3c.dom.Text

class AddMealplanRecipeActivity: AbstractPantryAppActivity() {
    private lateinit var recipeName: String
    private lateinit var mealplanDate: String

    private var foodList = ArrayList<FoodData>()
    private var recipeIndex = 0

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_mealplan_recipe)

        // Extract the extras from intent.
        recipeName = intent.extras!!.getString("recipeName")!!
        recipeIndex = intent.extras!!.getInt("recipeIndex")!!
        mealplanDate = intent.extras!!.getString("mealplanDate")!!

        // Support bar attributes.
        supportActionBar?.title = recipeName
        supportActionBar?.subtitle = "Back"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //setup layout to look like normal recipe detail view


        // Set up recycler view to show food list in this recipe.
        val recyclerView = mealplan_recipe_food_recycler_view
        val adapter = RecipeFoodListAdapter(foodList, recipeName)
        recyclerView.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        // Attach observer to recipe data.
        viewModel.recipeList.observe(this, Observer { liveData ->
            // Set up recipe details.
            val recipeData = liveData[recipeIndex]
            mealplan_recipeFoodList_serving_textView.text = "${recipeData.getFoodCalories()} Calories"
            mealplan_recipeFoodList_quantity_textView.text =
                "${recipeData?.getFoodTypeCount()} Food Types / ${recipeData.getFoodTotalCount()} Total"
            mealplan_recipeFoodList_rating_ratingBar.rating = recipeData.rating.toFloat()
            if (recipeData.imageLink == null) {
                mealplan_recipeFoodList_recipe_imageView.setImageResource(R.drawable.no_image_icon)
            } else {
                val imageRef = Firebase.storage.reference.child(recipeData.imageLink)
                imageRef.downloadUrl.addOnSuccessListener {
                    Picasso.get()
                        .load(it)
                        .transform(CropSquareTransformation())
                        .transform(RoundedCornersTransformation(25, 0))
                        .placeholder(R.drawable.loading_icon).into(mealplan_recipeFoodList_recipe_imageView)
                }.addOnFailureListener {
                    mealplan_recipeFoodList_recipe_imageView.setImageResource(R.drawable.no_image_icon)
                }
            }
            // Set up food list details.
            if (recipeData.foodList!!.isEmpty()) {
                mealplan_recipeFoodNoItems_textView.visibility = View.VISIBLE
                mealplan_recipeFood_foodLabel_textView.visibility = View.INVISIBLE
            } else {
                mealplan_recipeFoodNoItems_textView.visibility = View.INVISIBLE
                mealplan_recipeFood_foodLabel_textView.visibility = View.VISIBLE
            }
            foodList.clear()
            foodList.addAll(recipeData.foodList as Collection<FoodData>)
            Log.d("foodList ",foodList.toString())
            adapter.notifyDataSetChanged()
        })



        addRecipeDetail_addRecipeConfirm_button.isEnabled = true

        // Add recipe button listener.
        addRecipeDetail_addRecipeConfirm_button.setOnClickListener {
            val mealPlanData = MealplanData(mealplanDate, emptyList())
            viewModel.getDates()
            viewModel.getRecipeIngredients(recipeName)
            viewModel.ingredientsList.observe(this, Observer{
                val ingredientList = it
                val ingredientPurchaseAmounts = checkIngredientPurchaseNeeded(ingredientList)
                if (ingredientPurchaseAmounts.size == 0){ //if no ingredients need to be purchased
                    addRecipeToCurrentDayHelper(mealPlanData)
                    // Return to previous activity.
                    onBackPressed()
                } else {
                    //dispay dialog asking if user wants to add items to shopping list
                    //this function also handles returning to the previous activity
                    ingredientDialog(ingredientPurchaseAmounts)
                    //use helper to make addition to database
                    addRecipeToCurrentDayHelper(mealPlanData)
                }
            })
        }

        // Cancel button listener.
        addRecipeDetail_addRecipeCancel_button.setOnClickListener {
            // Return to recipe search list.
            onBackPressed()
        }
    }

    // Return to previous activity.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    fun checkIfDateExists(): Boolean{
        var dateExists = false
        val currentWeekDates = viewModel.dateList.value
        for (i in 0 until viewModel.dateList.value!!.size){
            if(currentWeekDates!![i].date == mealplanDate) {
                dateExists = true
            }
        }
        return dateExists
    }

    fun checkIngredientPurchaseNeeded(ingredientList: List<FoodData>): ArrayList<Pair<String, Long>>{
        val ingredientPurchaseQuantities = ArrayList<Pair<String, Long>>()
        val pantries = viewModel.pantryList.value
        for(ingredient in ingredientList) {
            var currentQuantity = ingredient.quantity as Long
            if (pantries != null) {
                for (pantry in pantries) {
                    val pantryFoodList = pantry.foodList
                    if (pantryFoodList != null) {
                        for (food in pantryFoodList) {
                            if (food.name == ingredient.name) {
                                currentQuantity -= food.quantity
                            }
                        }
                    }
                }
            }
            if(currentQuantity > 0) {
                ingredientPurchaseQuantities.add(Pair(ingredient.name, currentQuantity))
            }
        }
        return ingredientPurchaseQuantities
    }

    private fun addRecipeToCurrentDayHelper(mealPlanData: MealplanData){
        var dateExists = checkIfDateExists()
        if(dateExists){
            viewModel.addRecipeToDate(mealplanDate,recipeName)
        }else{
            viewModel.addDate(mealPlanData.getDataMap())
            viewModel.dateList.observe(this, Observer{
                viewModel.addRecipeToDate(mealplanDate,recipeName)
            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun ingredientDialog(ingredientPurchaseAmounts: List<Pair<String, Long>>){
        val addIngredientsConfirmDialog = LayoutInflater.from(this).inflate(
            R.layout.dialog_add_ingredient_to_shopping, null
        )
        val dialogBuilder = android.app.AlertDialog.Builder(this)
            .setView(addIngredientsConfirmDialog)
        val dialog = dialogBuilder.show()

        // Update the remove food message.
        val messageView: TextView = dialog.findViewById(R.id.addIngredientMessage_textView)
        val message = "You don't have these ingredients for this recipe in your pantry. Add missing ingredients to shopping list?"
        messageView.text = message

        //Show all ingredients to potentially add to shopping list
        val ingredientsView: TextView = dialog.findViewById(R.id.addIngredientList_textView)
        var ingredientsString = ""
        for (purchase in ingredientPurchaseAmounts){
            ingredientsString+= purchase.first.toString() + ": " + purchase.second.toString() + ", "
        }

        ingredientsView.text = ingredientsString.dropLast(2)

//                 User confirms addition.
        dialog.addIngredientConfirm_button.setOnClickListener {
            // Define views.
            val confirmButton: Button = dialog.findViewById(R.id.addIngredientConfirm_button)
            val cancelButton: Button = dialog.findViewById(R.id.addIngredientCancel_button)

            // Extract the quantity of item to add.

            for(ingredient in ingredientPurchaseAmounts){
                val item = ShoppingData(ingredient.first, ingredient.second).getDataMap()
                viewModel.addShoppingListItem(item)
            }

            // Disable the buttons once a request has been made.
            confirmButton.isEnabled = false
            cancelButton.isEnabled = false
            // Delete the selected pantry.
            dialog.dismiss()
            onBackPressed()
        }

        dialog.addIngredientCancel_button.setOnClickListener {
            dialog.dismiss()
            onBackPressed()
        }
    }

}