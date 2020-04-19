package com.example.pantry_organizer.recipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.FoodData
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

class RecipeFoodListAdapter(private val list: ArrayList<FoodData>?): RecyclerView.Adapter<RecipeFoodListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeFoodListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RecipeFoodListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: RecipeFoodListViewHolder, position: Int) {
        // Extract the food data from the position in the list.
        val foodData = list!![position]

        holder.bind(foodData)
        // todo onclick method for food list item select
        holder.itemView.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString("FoodName",foodName)
//
//            //start recipe detail fragment
//            val activity = view!!.context as AppCompatActivity
//            val fragment = FoodDetailFragment()
//            fragment.setArguments(bundle)
//
//            val ft: FragmentTransaction = activity.getSupportFragmentManager().beginTransaction()
//            ft.replace(R.id.home_frameLayout, fragment)
//            ft.commit()
        }
    }

    override fun getItemCount(): Int{
        return list!!.size
    }
}

class RecipeFoodListViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_recipe_food_list_item, parent, false)) {
    fun bind(foodData: FoodData?) {
        // Get view objects.
        val foodNameView: TextView = itemView.findViewById(R.id.adapter_recipeFoodList_foodName_textView)
        val foodServingSizeView: TextView = itemView.findViewById(R.id.adapter_recipeFoodList_foodServingSize_textView)
        val foodQtyView: TextView = itemView.findViewById(R.id.adapter_recipeFoodList_foodQuantity_textView)
        val foodImageView: ImageView = itemView.findViewById(R.id.adapter_recipeFoodList_foodImage_imageView)

        // Update view object data.
        foodNameView.text = foodData?.name
        foodServingSizeView.text = "Serving Size: ${foodData?.getServingSize()}"
        foodQtyView.text = "Quantity: ${foodData?.quantity}"

        // Update image view data.
        if (foodData?.imageLink == null) {
            foodImageView.setImageResource(R.drawable.no_image_icon)
        } else {
            Picasso.get()
                .load(foodData.imageLink)
                .error(R.drawable.no_image_icon)
                .transform(CropSquareTransformation())
                .transform(RoundedCornersTransformation(5, 0))
                .placeholder(R.drawable.loading_icon).into(foodImageView)
        }
    }
}
