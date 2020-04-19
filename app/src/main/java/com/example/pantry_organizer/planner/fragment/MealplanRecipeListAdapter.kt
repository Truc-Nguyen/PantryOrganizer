package com.example.pantry_organizer.planner.fragment

import android.content.Context
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
import com.example.pantry_organizer.recipe.fragment.RecipeDetailFragment

class MealplanRecipeListAdapter(private val list: ArrayList<String>?): RecyclerView.Adapter<MealplanRecipeListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealplanRecipeListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MealplanRecipeListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MealplanRecipeListViewHolder, position: Int) {
        val recipe: String = holder.bind(list!![position])
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                //store date string
                val bundle = Bundle()

                //do not change label, matches that used in RecipeListFragment
                bundle.putString("RecipeName",recipe)

                //start recipe detail fragment
                val activity = view!!.context as AppCompatActivity
                val fragment = RecipeDetailFragment()
                fragment.setArguments(bundle)

                val ft: FragmentTransaction = activity.getSupportFragmentManager().beginTransaction()
                //need to replace home_frameLayout with frame layout of mealplan detail view
                ft.replace(R.id.home_frameLayout, fragment)
                ft.commit()
            }
        })
    }

    override fun getItemCount(): Int {
        return list!!.size
    }
}

class MealplanRecipeListViewHolder(inflater: LayoutInflater, parent: ViewGroup):
RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_mealplan_receipe_list_item, parent, false)) {
    fun bind(mealplanRecipe: String): String {
        val mealplanRecipeText: TextView = itemView.findViewById(R.id.mealplan_recipe_name)
        mealplanRecipeText.text = mealplanRecipe

        return mealplanRecipe
    }
}