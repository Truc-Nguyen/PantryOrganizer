package com.example.pantry_organizer.recipe.activity

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
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
import kotlinx.android.synthetic.main.activity_recipe_food_list.*
import kotlinx.android.synthetic.main.dialog_confirm_remove_food.*

class RecipeFoodListActivity: AbstractPantryAppActivity() {
    private lateinit var recipeName: String
    private var recipeIndex = 0
    private var foodList = ArrayList<FoodData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_food_list)

        // Extract the extras from intent.
        recipeName = intent.extras!!.getString("recipeName")!!
        recipeIndex = intent.extras!!.getInt("recipeIndex")

        // Support bar attributes.
        supportActionBar?.title = recipeName
        supportActionBar?.subtitle = "Home"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up recycler view to show food list in this recipe.
        val recyclerView = recipe_food_recycler_view
        val adapter = RecipeFoodListAdapter(foodList, recipeName)
        recyclerView.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        // Set up delete item swipe controller and button listeners.
        val swipeController = SwipeController(this, 175f, object: SwipeControllerActions() {
            override fun setOnDeleteClicked(position: Int) {
                // Build an alert dialog for deleting this item.
                val removeFoodQuantityConfirmDialog = LayoutInflater.from(this@RecipeFoodListActivity).inflate(
                    R.layout.dialog_confirm_remove_food, null)
                val dialogBuilder = AlertDialog.Builder(this@RecipeFoodListActivity)
                    .setView(removeFoodQuantityConfirmDialog)
                val dialog = dialogBuilder.show()

                // Update the remove food message.
                val messageView: TextView = dialog.findViewById(R.id.removeFoodMessage_textView)
                val message = "Are you sure you want to remove ${foodList[position].name} from $recipeName?"
                messageView.text = message

                // User confirms deletion.
                dialog.removeFoodConfirm_button.setOnClickListener{
                    // Define views.
                    val qtyView: EditText = dialog.findViewById(R.id.removeFoodQuantity_editText)
                    val confirmButton: Button = dialog.findViewById(R.id.removeFoodConfirm_button)
                    val cancelButton: Button = dialog.findViewById(R.id.removeFoodCancel_button)

                    // Reset quantity field color.
                    qtyView.background = resources.getDrawable(R.drawable.edit_text_border, null)

                    // Sanitize input.
                    val input = qtyView.text.toString()
                    if (input == "") {
                        Toast.makeText(this@RecipeFoodListActivity, "Please enter a valid quantity.", Toast.LENGTH_LONG).show()
                        qtyView.background = resources.getDrawable(R.drawable.edit_text_border_red, null)
                        return@setOnClickListener
                    }

                    // Extract the quantity of food to remove.
                    val qtyToRemove = input.toInt()

                    // Disable the buttons once a request has been made.
                    confirmButton.isEnabled = false
                    cancelButton.isEnabled = false

                    // Delete the selected recipe.
                    viewModel.removeFoodQtyFromRecipe(recipeName, foodList[position], qtyToRemove)

                    dialog.dismiss()
                }

                // User selects cancel.
                dialog.removeFoodCancel_button.setOnClickListener {
                    dialog.dismiss()
                }
            }
        })

        // Attach the swipe controller to the recycler view.
        ItemTouchHelper(swipeController).attachToRecyclerView(recyclerView)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController.onDraw(c)
            }
        })

        // Attach observer to recipe data.
        viewModel.recipeList.observe(this, Observer { liveData ->
            foodList.clear()
            foodList.addAll(liveData[recipeIndex].foodList as Collection<FoodData>)
            adapter.notifyDataSetChanged()
        })
    }

    // Inflate the app menu.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_food_menu, menu)
        return true
    }

    // App menu item listeners.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addFood_menuItem -> {
                val intent = Intent(this, ApiFoodSearchActivity::class.java)
                intent.putExtra("recipeName", recipeName)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Return to previous activity.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}