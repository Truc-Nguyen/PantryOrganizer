package com.example.pantry_organizer.planner.fragment

import android.content.Context
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
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                //store date string
                val bundle = Bundle()
                bundle.putString("MealplanDate",mealplanDate)

                //start recipe detail fragment
                val activity = view!!.context as AppCompatActivity
                val fragment = MealplanDetailFragment()
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