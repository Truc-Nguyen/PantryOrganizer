package com.example.pantry_organizer.planner.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.RecipeData

class RecipeSearchAdapter(private val list: ArrayList<RecipeData>?, private val date: String)
    :RecyclerView.Adapter<MealplanRecipeSearchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealplanRecipeSearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MealplanRecipeSearchViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MealplanRecipeSearchViewHolder, position: Int) {
        // Extract the api food data from the position in the list.
        val recipeData = list!![position]

        // Bind api food data at this position to the recycler view item.
        holder.bind(recipeData.name)

        // Set click listener for viewing nutritional data for this food.
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, AddMealplanRecipeActivity::class.java)
            intent.putExtra("recipeName", recipeData.name)
            intent.putExtra("mealplanDate", date)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }
}

class MealplanRecipeSearchViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_mealplan_receipe_list_item, parent, false)) {
    fun bind(name: String?) {
        // Get view objects.
        val recipeNameView: TextView = itemView.findViewById(R.id.mealplan_recipe_name)
        // Update view object data.
        recipeNameView.text = name

    }
}