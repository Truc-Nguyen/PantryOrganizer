package com.example.pantry_organizer.planner.fragment

import android.content.Context
import android.content.Intent
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
import com.example.pantry_organizer.data.MealplanData
import com.example.pantry_organizer.data.RecipeData
import com.example.pantry_organizer.pantry.activity.AddFoodActivity
import com.example.pantry_organizer.pantry.activity.PantryFoodListActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation


class MealplanListAdapter(private val list: ArrayList<MealplanData>?): RecyclerView.Adapter<MealplanListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealplanListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MealplanListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MealplanListViewHolder, position: Int) {
        val mealplanDate: String = holder.bind(list!![position])
        // Set click listener for viewing nutritional data for this food.
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, MealplanDetailActivity::class.java)
            intent.putExtra("MealplanDate", mealplanDate)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }
}

class MealplanListViewHolder(inflater: LayoutInflater, parent: ViewGroup):
RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_mealplan_list_item, parent, false)) {
    fun bind(mealplanData: MealplanData): String {
        val mealplanDateView: TextView = itemView.findViewById(R.id.adapter_mealplan_date)
        val mealplanRecipesView: TextView = itemView.findViewById(R.id.adapter_mealplan_recipes)

        //parse mealplanDateView; currently stored as string in format: "yyyy-MM-dd"
        val parsedDate = mealplanData.date.split(",")
        mealplanDateView.text = parsedDate[1] + "/" + parsedDate[2]

        //set recipes to be multiline string
        var recipeString = ""
        for (recipe in mealplanData.recipes!!){
            recipeString += recipe + ",\n"
        }
        mealplanRecipesView.text = recipeString

        return mealplanData.date
    }
}