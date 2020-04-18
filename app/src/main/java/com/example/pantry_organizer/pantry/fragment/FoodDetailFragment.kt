package com.example.pantry_organizer.pantry.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pantry_organizer.R
import com.example.pantry_organizer.global.viewModel.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_food_detail.*


class FoodDetailFragment: Fragment() {
    lateinit var viewModel: ViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val activity = this.activity as AppCompatActivity
        // Support bar attributes.
        activity.supportActionBar?.title = "Food Detail"
        activity.supportActionBar?.subtitle = "Back"
        activity.supportActionBar?.setHomeButtonEnabled(true)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        return inflater.inflate(R.layout.fragment_food_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)

        //retrieve arguments from previous fragment
        val bundle = this.arguments
        val foodName = bundle!!.getString("FoodName", "food")

        // get single recipe through viewmodel
      //  viewModel.getSingleFood(foodName)
        viewModel.singleFood.observe(this, Observer{

            val activity = this.activity as AppCompatActivity
            //set action bar appropriately
            activity.supportActionBar?.title = it.food_name

            food_detail_calories.text = it.nf_calories.toString()
            food_detail_serving_size.text = it.serving_qty + " " + it.serving_unit
            food_detail_carbs.text = it.nf_total_carbohydrate.toString()
            food_detail_fat.text = it.nf_total_fat.toString()
            food_detail_protein.text = it.nf_protein.toString()
            food_detail_sugar.text = it.nf_sugars.toString()


//            //upload image
//            if (it.photo!!.highres == null) { //.highres!!
//                food_detail_imageView.setImageResource(R.drawable.no_image_icon)
//            } else {
//                val imageRef = Firebase.storage.reference.child(it.photo.highres!!) //.highres!!
//
//                imageRef.downloadUrl.addOnSuccessListener {
//                    Picasso.get()
//                        .load(it)
//                        .transform(CropSquareTransformation())
//                        .transform(RoundedCornersTransformation(100, 0))
//                        .placeholder(R.drawable.loading_icon).into(food_detail_imageView)
//                }.addOnFailureListener {
//                    food_detail_imageView.setImageResource(R.drawable.no_image_icon)
//                }
//            }

        })
    }


}