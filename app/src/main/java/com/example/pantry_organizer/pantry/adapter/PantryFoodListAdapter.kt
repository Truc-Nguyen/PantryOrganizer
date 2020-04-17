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
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.RecipeData
import com.example.pantry_organizer.pantry.fragment.FoodDetailFragment
import com.example.pantry_organizer.recipe.fragment.RecipeDetailFragment
import com.example.pantry_organizer.recipe.fragment.RecipeListViewHolder
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

//create the view holder
class PantryFoodListViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_food_list_item, parent, false)) {

    fun bind(foodNameAndAmount: List<String>): String {
//fun bind(foodNameAndAmount: HashMap<String, String>): String {

    val foodNameView: TextView = itemView.findViewById(R.id.pantry_food_name)
        val foodAmountView: TextView = itemView.findViewById(R.id.pantry_food_amount)

        foodNameView.text = foodNameAndAmount[0]
        foodAmountView.text = foodNameAndAmount[1]

        return foodNameAndAmount[0]
    }

}

//create the listener for the recycler view
class PantryFoodListAdapter(private val list: List<List<String>>?) : RecyclerView.Adapter<PantryFoodListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PantryFoodListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PantryFoodListViewHolder(
            inflater,
            parent
        )
    }

    override fun onBindViewHolder(holder: PantryFoodListViewHolder, position: Int) {
       // val test1 = list!![0].toList() as Pair<String,String>
////        val test2 = list!![position]
//
        Log.d("test", list.toString())
       // Log.d("test", test2)

        val foodName: String = holder.bind(list!![position])
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


