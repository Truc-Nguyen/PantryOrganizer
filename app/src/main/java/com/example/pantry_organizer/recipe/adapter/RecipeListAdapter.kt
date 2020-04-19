package com.example.pantry_organizer.recipe.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.RecipeData
import com.example.pantry_organizer.recipe.activity.RecipeFoodListActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

class RecipeListAdapter(private val list: ArrayList<RecipeData>?): RecyclerView.Adapter<RecipeListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RecipeListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: RecipeListViewHolder, position: Int) {
        // Extract the recipe data from the position in the list.
        val recipeData = list!![position]

        // Bind recipe data at this position to recycler view item.
        holder.bind(recipeData)

        // Set click listener for starting food list activity for this recipe.
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, RecipeFoodListActivity::class.java)
            intent.putExtra("recipeName", recipeData.name)
            intent.putExtra("recipeIndex", position)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }
}

class RecipeListViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_recipe_list_item, parent, false)) {
    fun bind(recipeData: RecipeData?) {
        // Get view objects.
        val recipeNameView: TextView = itemView.findViewById(R.id.adapter_recipeList_recipeName_textView)
        val recipeCaloriesView: TextView = itemView.findViewById(R.id.adapter_recipeList_recipeCalories_textView)
        val recipeFoodCountView: TextView = itemView.findViewById(R.id.adapter_recipeList_recipeFoodCount_textView)
        val recipeRatingBarView: RatingBar = itemView.findViewById(R.id.adapter_recipeList_recipeRating_ratingBar)
        val recipeImageView: ImageView = itemView.findViewById(R.id.adapter_recipeList_recipeImage_imageView)

        // Update view object data.
        recipeNameView.text = recipeData?.name
        recipeCaloriesView.text = recipeData?.getFoodCalories().toString()
        recipeFoodCountView.text =
            "${recipeData?.getFoodTypeCount()} Food Types / ${recipeData?.getFoodTotalCount()} Total"
        recipeRatingBarView.rating = recipeData?.rating?.toFloat()!!

        // Update image view data.
        if (recipeData.imageLink == null) {
            recipeImageView.setImageResource(R.drawable.no_image_icon)
        } else {
            val imageRef = Firebase.storage.reference.child(recipeData.imageLink)
            imageRef.downloadUrl.addOnSuccessListener {
                Picasso.get()
                    .load(it)
                    .transform(CropSquareTransformation())
                    .transform(RoundedCornersTransformation(25, 0))
                    .placeholder(R.drawable.loading_icon).into(recipeImageView)
            }.addOnFailureListener {
                recipeImageView.setImageResource(R.drawable.no_image_icon)
            }
        }
    }
}