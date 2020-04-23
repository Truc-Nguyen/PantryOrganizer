package com.example.pantry_organizer.planner.fragment.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.FoodData
import com.example.pantry_organizer.data.MealplanData
import com.example.pantry_organizer.data.RecipeData
import com.example.pantry_organizer.data.ShoppingData
import com.example.pantry_organizer.global.activity.AbstractPantryAppActivity
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_add_food.*
import kotlinx.android.synthetic.main.activity_add_mealplan_recipe.*
import kotlinx.android.synthetic.main.dialog_add_ingredient_to_shopping.*
import kotlinx.android.synthetic.main.dialog_add_item_to_shopping.*
import org.w3c.dom.Text

class AddMealplanRecipeActivity: AbstractPantryAppActivity() {
    private lateinit var recipeName: String
    private lateinit var mealplanDate: String

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_mealplan_recipe)

        // Extract the extras from intent.
        recipeName = intent.extras!!.getString("recipeName")!!
        mealplanDate = intent.extras!!.getString("mealplanDate")!!


        // Support bar attributes.
        supportActionBar?.title = recipeName
        supportActionBar?.subtitle = "Back"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Turn off the add food button until the data has been fetched.
        addRecipeDetail_addRecipeConfirm_button.isEnabled = false

            addRecipeDetail_addRecipeConfirm_button.isEnabled = true
//        })

        // Add to pantry button listener.
        addRecipeDetail_addRecipeConfirm_button.setOnClickListener {
            val mealPlanData = MealplanData(mealplanDate, emptyList())
            viewModel.getDates()
            viewModel.getRecipeIngredients(recipeName)
            viewModel.ingredientsList.observe(this, Observer{
                val ingredientList = it
                val ingredientPurchaseAmounts = checkIngredientPurchaseNeeded(ingredientList)
                if(ingredientPurchaseAmounts.size == 0){ //if no ingredients need to be purchased
                    addRecipeToCurrentDayHelper(mealPlanData)
                    // Return to previous activity.
                    onBackPressed()
                }else {
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
    fun checkIfDateExists(date: String): Boolean{
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
        var dateExists = checkIfDateExists(mealplanDate)
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
        var ingredientsString: String = ""
        for (purchase in ingredientPurchaseAmounts){
            ingredientsString+= purchase.first.toString() + ": " + purchase.second.toString() + ", "
        }
        Log.d("ingredientpurch",ingredientsString)

        ingredientsView.text = ingredientsString

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