package com.example.pantry_organizer.planner.fragment.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.RecipeData
import com.example.pantry_organizer.recipe.activity.RecipeFoodListActivity

class MealplanRecipeListAdapter(private val list: ArrayList<RecipeData>?): RecyclerView.Adapter<MealplanRecipeListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealplanRecipeListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MealplanRecipeListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MealplanRecipeListViewHolder, position: Int) {
        val recipeData = list!![position]
        holder.bind(recipeData)
        holder.itemView.setOnClickListener{
            val intent = Intent(it.context, RecipeFoodListActivity::class.java)
            intent.putExtra("pantryName", recipeData.name as String)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }
}

class MealplanRecipeListViewHolder(inflater: LayoutInflater, parent: ViewGroup):
RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_mealplan_receipe_list_item, parent, false)) {

    fun bind(mealplanRecipe: RecipeData) {
        val mealplanRecipeText: TextView = itemView.findViewById(R.id.mealplan_recipe_name)
        mealplanRecipeText.text = mealplanRecipe.name
    }
}