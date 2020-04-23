package com.example.pantry_organizer.planner.fragment.activity

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.global.activity.AbstractPantryAppActivity
import com.example.pantry_organizer.global.adapter.SwipeController
import com.example.pantry_organizer.global.adapter.SwipeControllerActions
import com.example.pantry_organizer.planner.fragment.adapter.MealplanRecipeListAdapter
import com.example.pantry_organizer.data.RecipeData
import kotlinx.android.synthetic.main.dialog_confirm_delete.*
import kotlinx.android.synthetic.main.activity_mealplan_detail.*


class MealplanDetailActivity: AbstractPantryAppActivity() {
    private var recipes = ArrayList<RecipeData>()
    private lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mealplan_detail)

        //retrieve arguments from previous fragment
        date = intent.extras!!.getString("MealplanDate")!!

        // Support bar attributes.
        supportActionBar?.title = date
        supportActionBar?.subtitle = "Meal Planner"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //display recipes using MealplanRecipeListAdapter
        val recyclerView = mealplan_recyclerView
        val adapter = MealplanRecipeListAdapter(recipes)
        recyclerView.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        //retrieve firebase recipes corresponding to date
        viewModel.dateRecipeList.observe(this, Observer { liveData ->
            recipes.clear()
            if (liveData != null)  {recipes.addAll(liveData.toList())}
            adapter.notifyDataSetChanged()
        })

        // implement swipe to delete
        val swipeController = SwipeController(this, 175f, object: SwipeControllerActions() {
            override fun setOnDeleteClicked(position: Int) {
                // Build an alert dialog for deleting this item.
                val deleteMealplanRecipeConfirmDialog = LayoutInflater.from(this@MealplanDetailActivity).inflate(R.layout.dialog_confirm_delete, null)
                val dialogBuilder = AlertDialog.Builder(this@MealplanDetailActivity)
                    .setView(deleteMealplanRecipeConfirmDialog)
                val dialog = dialogBuilder.show()
                // User confirms deletion.
                dialog.deleteItemConfirm_button.setOnClickListener{
                    dialog.dismiss()
                    // Delete the selected recipe from a meal plan
                    // implement delete function of recipe from a date
                    viewModel.removeRecipeFromDate(date,recipes[position])
                    viewModel.dateRecipeList.observe(this@MealplanDetailActivity, Observer { liveData ->
                        recipes.clear()
                        if (liveData != null){
                            recipes.addAll(liveData.toList())
                        }
                        adapter.notifyDataSetChanged()
                        adapter.notifyItemRemoved(position)
                    })
                }
                // User selects cancel.
                dialog.deleteItemCancel_button.setOnClickListener {
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

    }

    override fun onResume(){
        super.onResume()
        viewModel.getRecipesForDate(date!!)

    }

    // Inflate the app menu.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_recipe_menu, menu)
        return true
    }

    // App menu item listeners.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addRecipe_menuItem -> {
                val intent = Intent(this, MealplanRecipeSearchActivity::class.java)
                intent.putExtra("MealplanDate", date)
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