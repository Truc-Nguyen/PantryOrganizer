package com.example.pantry_organizer.recipe.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.ApiFoodData
import com.example.pantry_organizer.recipe.activity.AddFoodActivity
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

class ApiSearchAdapter(private val list: ArrayList<ApiFoodData>?, private val recipeName: String)
    :RecyclerView.Adapter<ApiSearchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApiSearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ApiSearchViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ApiSearchViewHolder, position: Int) {
        // Extract the api food data from the position in the list.
        val apiFoodData = list!![position]

        // Bind api food data at this position to the recycler view item.
        holder.bind(apiFoodData)

        // Set click listener for viewing nutritional data for this food.
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, AddFoodActivity::class.java)
            intent.putExtra("recipeName", recipeName)
            intent.putExtra("query", apiFoodData.food_name)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }
}

class ApiSearchViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_api_search_item, parent, false)) {
    fun bind(apiFoodData: ApiFoodData?) {
        // Get view objects.
        val foodNameView: TextView = itemView.findViewById(R.id.adapter_apiSearch_foodName_textView)
        val foodDetailsView: TextView = itemView.findViewById(R.id.adapter_apiSearch_foodDetails_textView)
        val foodImageView: ImageView = itemView.findViewById(R.id.adapter_apiSearch_foodImage_imageView)

        // Update view object data.
        foodNameView.text = apiFoodData?.food_name
        val servingQty = apiFoodData?.serving_quantity ?: "1"
        val servingUnit = apiFoodData?.serving_unit ?: "count"
        foodDetailsView.text = "$servingQty $servingUnit"

        // Update image view data.
        if (apiFoodData?.photo?.thumb == null) {
            foodImageView.setImageResource(R.drawable.no_image_icon)
        } else {
            Picasso.get()
                .load(apiFoodData.photo.thumb)
                .error(R.drawable.no_image_icon)
                .transform(CropSquareTransformation())
                .transform(RoundedCornersTransformation(5, 0))
                .placeholder(R.drawable.loading_icon).into(foodImageView)
        }
    }
}