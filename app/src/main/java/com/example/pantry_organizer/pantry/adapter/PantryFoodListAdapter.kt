package com.example.pantry_organizer.pantry.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.FoodData
import com.example.pantry_organizer.pantry.fragment.FoodDetailFragment

class PantryFoodListAdapter(private val list: ArrayList<FoodData>?) : RecyclerView.Adapter<PantryFoodListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PantryFoodListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PantryFoodListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PantryFoodListViewHolder, position: Int) {
        // Extract the food data from the position in the list.
        val foodData = list!![position]

        holder.bind(foodData)
        // todo onclick method for food list item select
        holder.itemView.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString("FoodName",foodName)
//
//            //start recipe detail fragment
//            val activity = view!!.context as AppCompatActivity
//            val fragment = FoodDetailFragment()
//            fragment.setArguments(bundle)
//
//            val ft: FragmentTransaction = activity.getSupportFragmentManager().beginTransaction()
//            ft.replace(R.id.home_frameLayout, fragment)
//            ft.commit()
        }
    }

    override fun getItemCount(): Int{
        return list!!.size
    }
}

class PantryFoodListViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_food_list_item, parent, false)) {
    fun bind(foodData: FoodData?) {
        // Get view objects.
        val foodNameView: TextView = itemView.findViewById(R.id.adapter_pantryFoodList_foodName_textView)

        foodNameView.text = foodData?.name
    }
}
