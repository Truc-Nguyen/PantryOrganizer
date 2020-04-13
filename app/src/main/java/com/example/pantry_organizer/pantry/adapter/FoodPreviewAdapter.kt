package com.example.pantry_organizer.pantry.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.FoodData
import com.squareup.picasso.Picasso

interface OnFoodItemClickListener{
    fun onFoodItemClicked(food: FoodData)
}

//create the view holder
class FoodPreviewViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_food_list_item, parent, false)) {

    private val food_name : TextView
    private val food_img: ImageView

    init {
        food_name = itemView.findViewById(R.id.food_preview_name)
        food_img = itemView.findViewById(R.id.food_preview_picture)
    }

    fun bind(food: FoodData,clickListener: OnFoodItemClickListener) {
        food_name.text = food!!.name
        val picasso = Picasso.get()
        val trackImgUrl = food.imageLink
        picasso.load(trackImgUrl).into(food_img)

        itemView.setOnClickListener {
            clickListener.onFoodItemClicked(food)
        }
    }

}

//create the listener for the recycler view
class FoodDataAdapter(private val list: ArrayList<FoodData>?, val itemClickListener: OnFoodItemClickListener)
    : RecyclerView.Adapter<FoodPreviewViewHolder>() {
    private var listEvents : ArrayList<FoodData>? = list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodPreviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FoodPreviewViewHolder(
            inflater,
            parent
        )
    }

    //bind the object
    override fun onBindViewHolder(holder: FoodPreviewViewHolder, position: Int) {
        val event: FoodData = listEvents!!.get(position)
        holder.bind(event,itemClickListener)
    }

    //set the count
    override fun getItemCount(): Int = list!!.size

}


