package com.example.pantry_organizer.planner.fragment.adapter

import android.graphics.Color
import android.os.Build
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.MealplanData
import com.example.pantry_organizer.planner.fragment.activity.MealplanDetailActivity
import java.time.LocalDate


class MealplanListAdapter(private val list: ArrayList<MealplanData>?): RecyclerView.Adapter<MealplanListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealplanListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MealplanListViewHolder(
            inflater,
            parent
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MealplanListViewHolder, position: Int) {
        val mealplanDate: String = holder.bind(list!![position])
        // Set click listener for viewing nutritional data for this food.
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, MealplanDetailActivity::class.java)

            intent.putExtra("mealplanDate", mealplanDate)

            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }
}

class MealplanListViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_mealplan_list_item, parent, false)) {
    private val maxRecipeLength = 24 //longest recipe name that can fit on one line of the display

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(mealplanData: MealplanData): String {
        val mealplanDateView: TextView = itemView.findViewById(R.id.adapter_mealplan_date)
        val mealplanWeedayView: TextView = itemView.findViewById(R.id.adapter_mealplan_weekday)
        val mealplanRecipesView: TextView = itemView.findViewById(R.id.adapter_mealplan_recipes)
        val parsedDate = mealplanData.date.split(".")
        val date = LocalDate.of(parsedDate[2].toInt(), parsedDate[0].toInt(), parsedDate[1].toInt())
        val weekDay = date.dayOfWeek.toString()
        val currentDate = LocalDate.now()
        val view: CardView =  itemView.findViewById(R.id.adapter_mealplan_layout)
        if(currentDate == date){
            view.setCardBackgroundColor(Color.rgb(200,200,200))
        }else{
            view.setCardBackgroundColor(Color.rgb(255,255,255))
        }

        mealplanDateView.text =  parsedDate[0] + "/" + parsedDate[1]
        mealplanWeedayView.text = weekDay

        //set recipes to be multiline string
        var recipeString = ""
        if(mealplanData.recipes != null) {
            for (recipe in mealplanData.recipes?.indices){
                    var currentRecipe = mealplanData.recipes!![recipe].name
                    if(currentRecipe.length > maxRecipeLength){  //if recipe is too long, truncate
                        currentRecipe = currentRecipe.take(maxRecipeLength - 3) + "..."
                    }
                    recipeString += currentRecipe
                    if (recipe < mealplanData.recipes!!.size - 1) {
                        recipeString += ",\n"
                    }
                }
            }
        mealplanRecipesView.text = recipeString
        return mealplanData.date
    }
}