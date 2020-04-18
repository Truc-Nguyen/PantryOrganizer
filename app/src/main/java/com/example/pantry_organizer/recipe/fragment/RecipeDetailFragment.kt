package com.example.pantry_organizer.recipe.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pantry_organizer.R
import com.example.pantry_organizer.global.viewModel.AppViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_recipe_detail.*


class RecipeDetailFragment: Fragment() {
    lateinit var viewModel: AppViewModel


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
        viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)

        //retrieve arguments from previous fragment
        val bundle = this.arguments
        val recipeName = bundle!!.getString("RecipeName", "Recipe")

        // get single recipe through viewmodel
  //      viewModel.getSingleRecipe(recipeName)
        viewModel.singleRecipe.observe(this, Observer{

            val activity = this.activity as AppCompatActivity
            //set action bar appropriately
            activity.supportActionBar?.title = it.name
            detail_recipeIngredients_textView.text = it.ingredientsList

            //upload image
            if (it.imageLink == null) {
                detail_recipeImage_imageView.setImageResource(R.drawable.no_image_icon)
            } else {
                val imageRef = Firebase.storage.reference.child(it.imageLink)

                imageRef.downloadUrl.addOnSuccessListener {
                    Picasso.get()
                        .load(it)
                        .transform(CropSquareTransformation())
                        .transform(RoundedCornersTransformation(100, 0))
                        .placeholder(R.drawable.loading_icon).into(detail_recipeImage_imageView)
                }.addOnFailureListener {
                    detail_recipeImage_imageView.setImageResource(R.drawable.no_image_icon)
                }
            }

        })

        detail_recipe_add_to_meals_button.setOnClickListener {
            //add to meals functionality
        }
    }
}