package com.example.pantry_organizer.pantry.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.FoodData

interface OnFoodItemClickListener{
    fun onFoodItemClicked(food: FoodData)
}

//create the view holder
class FoodViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.fragment_food_item, parent, false)) {

    private val food_name : TextView
    private val amount : TextView
    private val calories : TextView

    init {
        food_name = itemView.findViewById(R.id.pantry_food_name)
        amount = itemView.findViewById(R.id.pantry_food_amount)
        calories = itemView.findViewById(R.id.pantry_food_calories)
    }

    fun bind(food: FoodData,clickListener: OnFoodItemClickListener) {
        food_name.text = food!!.name
//        amount.text =
//        calories.text =

        itemView.setOnClickListener {
            clickListener.onFoodItemClicked(food)

        }
    }

}

//create the listener for the recycler view
class FoodDataAdapter(private val list: ArrayList<FoodData>?, val itemClickListener: OnFoodItemClickListener)
    : RecyclerView.Adapter<FoodViewHolder>() {
    private var listEvents : ArrayList<FoodData>? = list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FoodViewHolder(
            inflater,
            parent
        )
    }

    //bind the object
    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val event: FoodData = listEvents!!.get(position)
        holder.bind(event,itemClickListener)
    }

    //set the count
    override fun getItemCount(): Int = list!!.size

}


