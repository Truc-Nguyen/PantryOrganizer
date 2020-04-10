package com.example.pantry_organizer.recipe.fragment

//be able to add and display recipes using trucs model

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.RecipeData
import com.example.pantry_organizer.global.viewModel.ViewModel
import kotlinx.android.synthetic.main.fragment_recipe_list.*
import androidx.fragment.app.Fragment

class RecipeListFragment: Fragment() {
    lateinit var viewModel: ViewModel
    private var recipeList: ArrayList<RecipeData> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recipe_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("recipelistfrag", "created")
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)

        // Set up the recycler view to show pantry list.
        val recyclerView = recipe_recycler_view
        val adapter =
            RecipeListAdapter(
                recipeList
            )
        recyclerView.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(this.context)

        // Attach observer to pantry list.
        viewModel.recipeList.observe(this, Observer { liveData ->
            recipeList.clear()
            recipeList.addAll(liveData)
            adapter.notifyDataSetChanged()
        })
    }
}