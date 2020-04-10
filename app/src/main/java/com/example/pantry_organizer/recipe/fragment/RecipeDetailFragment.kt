package com.example.pantry_organizer.recipe.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pantry_organizer.R
import com.example.pantry_organizer.global.viewModel.ViewModel
import kotlinx.android.synthetic.main.activity_add_pantry.*
import kotlinx.android.synthetic.main.fragment_recipe_detail.*


class RecipeDetailFragment: Fragment() {
    lateinit var viewModel: ViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val activity = this.activity as AppCompatActivity

        // Support bar attributes.
        activity.supportActionBar?.title = "Recipe Detail"
        activity.supportActionBar?.subtitle = "Back"
        activity.supportActionBar?.setHomeButtonEnabled(true)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        return inflater.inflate(R.layout.fragment_recipe_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("recipedetailfrag", "created")
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)

        //retrieve arguments from previous fragment
        val bundle = this.arguments
        val recipeName = bundle!!.getString("RecipeName", "Recipe")
        Log.d("recipe","fragment data transfer success")

        // get single recipe through viewmodel
        viewModel.getSingleRecipe(recipeName)
        viewModel.singleRecipe.observe(this, Observer{

            val activity = this.activity as AppCompatActivity
            //set action bar appropriately
            activity.supportActionBar?.title = it.name

            Log.d("recipedetailfrag",it.name)

            detail_recipeIngredients_textView.text = it.ingredientsList
            //detail_recipeImage_imageView = it.imageLink

        })
        Log.d("recipedetailfrag","observer finished")
    }
}