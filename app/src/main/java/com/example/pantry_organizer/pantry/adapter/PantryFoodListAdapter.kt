package com.example.pantry_organizer.pantry.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.global.viewModel.ViewModel
import com.example.pantry_organizer.pantry.fragment.FoodDetailFragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_food_detail.*


//create the view holder
class PantryFoodListViewHolder(inflater: LayoutInflater, parent: ViewGroup, private val viewModel: ViewModel, private val owner: LifecycleOwner) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_food_list_item, parent, false)) {

    fun bind(foodNameAndAmount: String): String {
        val list = foodNameAndAmount.split(",")

        val foodNameView: TextView = itemView.findViewById(R.id.pantry_food_name)
        val foodAmountView: TextView = itemView.findViewById(R.id.pantry_food_amount)
        val foodImageView: ImageView = itemView.findViewById(R.id.adapter_foodList_foodImage_imageView)

        foodNameView.text = list[0]
        foodAmountView.text = list[1]

        //retrieve Firebasefood
        viewModel.getSingleFood(list[0])
        Log.d("foodadapter", viewModel.singleFood.value.toString())
        viewModel!!.singleFood!!.observe(owner, Observer {
            Log.d("foodadapter",it.photo)
            // Update image view data.
            if (it.photo == null) {
                foodImageView.setImageResource(R.drawable.no_image_icon)
            }

            //Functionality for displaying photos not working yet


//            else {
//                val imageRef = Firebase.storage.reference.child(it.photo!!)
//                imageRef.downloadUrl.addOnSuccessListener {
//                    Picasso.get()
//                        .load(it)
//                        .transform(CropSquareTransformation())
//                        .transform(RoundedCornersTransformation(25, 0))
//                        .placeholder(R.drawable.loading_icon).into(foodImageView)
//                }.addOnFailureListener {
//                    foodImageView.setImageResource(R.drawable.no_image_icon)
//                }
//            }

//            val picasso = Picasso.get()
//            Log.d("foodadapter",it.photo.toString())
//            val trackImgUrl = it!!.photo!!
//            picasso.load(trackImgUrl).into(foodImageView)
        })

        return list[0]
    }

}

//create the listener for the recycler view
class PantryFoodListAdapter(private val list: List<String>, private val viewModel: ViewModel, private val owner: LifecycleOwner) : RecyclerView.Adapter<PantryFoodListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PantryFoodListViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return PantryFoodListViewHolder(inflater, parent,viewModel,owner)
    }


    override fun onBindViewHolder(holder: PantryFoodListViewHolder, position: Int) {
        Log.d("test", list.toString())
       // Log.d("test", test2)

        val foodName: String = holder.bind(list[position])
        holder.itemView.setOnClickListener(object : View.OnClickListener {

            //set onclick for food item in list
            override fun onClick(view: View?) {
                //store
                val bundle = Bundle()
                bundle.putString("FoodName",foodName)

                //start recipe detail fragment
                val activity = view!!.context as AppCompatActivity
                val fragment = FoodDetailFragment()
                fragment.setArguments(bundle)

                val ft: FragmentTransaction = activity.getSupportFragmentManager().beginTransaction()
                ft.replace(R.id.home_frameLayout, fragment)
                ft.commit()
            }
        })
    }

    //set the count
    override fun getItemCount(): Int{
        return list!!.size
    }

}


