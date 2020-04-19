package com.example.pantry_organizer.planner.fragment

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
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
import java.time.LocalDate


class MealplanListAdapter(private val list: ArrayList<MealplanData>?): RecyclerView.Adapter<MealplanListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealplanListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MealplanListViewHolder(inflater, parent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(mealplanData: MealplanData): String {
        val mealplanDateView: TextView = itemView.findViewById(R.id.adapter_mealplan_date)
        val mealplanRecipesView: TextView = itemView.findViewById(R.id.adapter_mealplan_recipes)
        //parse mealplanDateView; currently stored as string in format: ""M/d/y""
        val parsedDate = mealplanData.date.split("/")
        val date = LocalDate.of(parsedDate[2].toInt(), parsedDate[0].toInt(), parsedDate[1].toInt())
        val weekDay = date.dayOfWeek.toString()
        val currentDate = LocalDate.now()
        Log.d("Current Date", currentDate.toString())
        Log.d("Date", currentDate.toString())
        if(currentDate == date){
            val view: RelativeLayout =  itemView.findViewById(R.id.adapter_mealplan_layout)
            view.setBackgroundColor(Color.GREEN)
        }


        mealplanDateView.text = weekDay + " " +  parsedDate[0] + "/" + parsedDate[1]

        //set recipes to be multiline string
        var recipeString = ""
        if(mealplanData.recipes != null) {
            for (recipe in mealplanData.recipes?.indices){
                    recipeString += mealplanData.recipes!![recipe]
                    if (recipe < mealplanData.recipes!!.size - 1) {
                        recipeString += ",\n"
                    }
                }
            }
        mealplanRecipesView.text = recipeString

        return mealplanData.date
    }
}