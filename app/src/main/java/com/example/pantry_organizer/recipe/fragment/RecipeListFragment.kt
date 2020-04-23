package com.example.pantry_organizer.recipe.fragment

import android.app.AlertDialog
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.RecipeData
import com.example.pantry_organizer.global.adapter.SwipeController
import com.example.pantry_organizer.global.adapter.SwipeControllerActions
import com.example.pantry_organizer.global.viewModel.AppViewModel
import com.example.pantry_organizer.recipe.adapter.RecipeListAdapter
import kotlinx.android.synthetic.main.dialog_confirm_delete.*
import kotlinx.android.synthetic.main.fragment_recipe_list.*

class RecipeListFragment: Fragment() {
    lateinit var viewModel: AppViewModel
    private var recipeList = ArrayList<RecipeData>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recipe_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)

        // Set up the recycler view to show recipe list.
        val recyclerView = recipe_recyclerView
        val adapter = RecipeListAdapter(recipeList)
        recyclerView.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(this.context)

        // Set up delete item swipe controller and button listeners.
        val swipeController = SwipeController(activity!!, 175f, object: SwipeControllerActions() {
            override fun setOnDeleteClicked(position: Int) {
                // Build an alert dialog for deleting this item.
                val deleteRecipeConfirmDialog = LayoutInflater.from(activity!!).inflate(
                    R.layout.dialog_confirm_delete, null)
                val dialogBuilder = AlertDialog.Builder(activity!!)
                    .setView(deleteRecipeConfirmDialog)
                val dialog = dialogBuilder.show()

                // User confirms deletion.
                dialog.deleteItemConfirm_button.setOnClickListener{
                    dialog.dismiss()
                    // Delete the selected recipe.
                    viewModel.deleteRecipe(recipeList[position].name)
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

        // Attach observer to recipe data.
        viewModel.recipeList.observe(this, Observer { liveData ->
            if (liveData.isEmpty()) {
                recipeNoItems_textView.visibility = View.VISIBLE
            } else {
                recipeNoItems_textView.visibility = View.INVISIBLE
            }

            recipeList.clear()
            recipeList.addAll(liveData)
            adapter.notifyDataSetChanged()
        })
    }
}