package com.example.pantry_organizer.planner.fragment.activity

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Adapter
import androidx.annotation.RequiresApi
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
import kotlinx.android.synthetic.main.activity_recipe_food_list.*
import java.time.LocalDate


class MealplanDetailActivity: AbstractPantryAppActivity() {
    private var recipes = ArrayList<RecipeData>()
    private lateinit var mealplanDate: String
    private lateinit var adapter: MealplanRecipeListAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mealplan_detail)

        //retrieve arguments from previous fragment
        mealplanDate = intent.extras!!.getString("mealplanDate")!!

        //convert date to readable view
        val parsedDate = mealplanDate.split(".")
        val date = LocalDate.of(parsedDate[2].toInt(), parsedDate[0].toInt(), parsedDate[1].toInt())
        val weekDay = date.dayOfWeek.toString()
        val correctDate = parsedDate[0] + "/" + parsedDate[1]

        // Support bar attributes.
        supportActionBar?.title = weekDay + " " + correctDate
        supportActionBar?.subtitle = "Meal Planner"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //display recipes using MealplanRecipeListAdapter
        val recyclerView = mealplan_recyclerView
        adapter = MealplanRecipeListAdapter(recipes)
        recyclerView.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        //retrieve firebase recipes corresponding to date
        viewModel.dateRecipeList.observe(this, Observer { liveData ->
            recipes.clear()
            if (liveData != null){
                recipes.addAll(liveData.toList())
            }
            adapter.notifyDataSetChanged()

            //show prompt if no recipes exist
            if (recipes.size == 0) {
                mealplanRecipesNoItems_textView.visibility = View.VISIBLE
            } else {
                mealplanRecipesNoItems_textView.visibility = View.INVISIBLE
            }
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
                    viewModel.removeRecipeFromDate(mealplanDate,recipes[position])
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
        viewModel.getRecipesForDate(mealplanDate!!)

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
                intent.putExtra("MealplanDate", mealplanDate)
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