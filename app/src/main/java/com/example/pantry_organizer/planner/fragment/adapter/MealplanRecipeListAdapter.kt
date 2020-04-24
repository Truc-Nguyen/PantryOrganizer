package com.example.pantry_organizer.planner.fragment.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.FoodData
import com.example.pantry_organizer.data.RecipeData
import com.example.pantry_organizer.planner.fragment.activity.MealPlanFoodListActivity
import com.example.pantry_organizer.recipe.activity.RecipeFoodListActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

class MealplanRecipeListAdapter(private val list: ArrayList<RecipeData>?): RecyclerView.Adapter<MealplanRecipeListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealplanRecipeListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MealplanRecipeListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MealplanRecipeListViewHolder, position: Int) {
        val recipeData = list!![position]
        holder.bind(recipeData)
        holder.itemView.setOnClickListener{
            val intent = Intent(it.context, MealPlanFoodListActivity::class.java)
            intent.putExtra("recipeName", recipeData.name)
            intent.putExtra("recipeIndex", position)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }
}

class MealplanRecipeListViewHolder(inflater: LayoutInflater, parent: ViewGroup):
RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_mealplan_receipe_list_item, parent, false)) {

    fun bind(mealplanRecipe: RecipeData?) {
        val mealplanRecipeText: TextView = itemView.findViewById(R.id.adapter_mealplan_recipe_name)
        val mealPlanRecipeCaloriesView: TextView = itemView.findViewById(R.id.adapter_mealPlan_recipeCalories_textView)
        val mealPlanRecipeFoodCountView: TextView = itemView.findViewById(R.id.adapter_mealPlan_recipeFoodCount_textView)
        val mealPlanRecipeRatingBarView: RatingBar = itemView.findViewById(R.id.adapter_mealPlan_recipeRating_ratingBar)
        val recipeImageView: ImageView = itemView.findViewById(R.id.adapter_mealplan_recipeIcon_imageView)


        val foodMapList = mealplanRecipe!!.foodList
        val foodList: MutableList<FoodData> = mutableListOf()
        if(foodMapList != null){
            for(food in foodMapList as List<Map<String,Any>>){
                foodList.add(FoodData(food))
            }
        }
        val rectifiedRecipe =  RecipeData(
            mealplanRecipe.name,
            mealplanRecipe.imageLink,
            mealplanRecipe.recipeImageLink,
            mealplanRecipe.rating,
            foodList)


        //Populate list item information with recipe data
        mealplanRecipeText.text = mealplanRecipe?.name
        mealPlanRecipeCaloriesView.text = "${rectifiedRecipe?.getFoodCalories()} Calories"
        mealPlanRecipeFoodCountView.text = "${rectifiedRecipe?.getFoodTypeCount()} Food Types / ${rectifiedRecipe?.getFoodTotalCount()} Total"
        mealPlanRecipeRatingBarView.rating = rectifiedRecipe?.rating?.toFloat()!!

        if (mealplanRecipe.imageLink == null) {
            recipeImageView.setImageResource(R.drawable.recipe_icon)
        } else {
            val imageRef = Firebase.storage.reference.child(mealplanRecipe.imageLink)
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