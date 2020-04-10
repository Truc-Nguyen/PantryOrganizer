package com.example.pantry_organizer.recipe.fragment

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
import com.example.pantry_organizer.data.RecipeData
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation


class RecipeListAdapter(private val list: ArrayList<RecipeData>?): RecyclerView.Adapter<RecipeListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListViewHolder {


        val inflater = LayoutInflater.from(parent.context)
        return RecipeListViewHolder(
            inflater,
            parent
        )
    }

    override fun onBindViewHolder(holder: RecipeListViewHolder, position: Int) {
        val recipeName: String = holder.bind(list!![position])
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {

                Log.d("onclick", "succeeded")
                Log.d("adaptrecipename", recipeName)

                //store
                val bundle = Bundle()
                bundle.putString("RecipeName",recipeName)

                //start recipe detail fragment
                val activity = view!!.context as AppCompatActivity
                val fragment = RecipeDetailFragment()
                fragment.setArguments(bundle)

                val ft: FragmentTransaction = activity.getSupportFragmentManager().beginTransaction()
                Log.d("fragtransaction", "succeeded")
                ft.replace(R.id.home_frameLayout, fragment)
                ft.commit()
            }
        })
    }

    override fun getItemCount(): Int {
        return list!!.size
    }
}

class RecipeListViewHolder(inflater: LayoutInflater, parent: ViewGroup):
RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_recipe_list_item, parent, false)) {
    fun bind(recipeData: RecipeData): String {
        val recipeNameView: TextView = itemView.findViewById(R.id.adapter_recipeList_recipeName_textView)
        val recipeImageView: ImageView = itemView.findViewById(R.id.adapter_recipeList_recipeImage_imageView)

        recipeNameView.text = recipeData.name
        if (recipeData.imageLink == null) {
            recipeImageView.setImageResource(R.drawable.no_image_icon)
        } else {
            val imageRef = Firebase.storage.reference.child(recipeData.imageLink)

            imageRef.downloadUrl.addOnSuccessListener {
                Picasso.get()
                    .load(it)
                    .transform(CropSquareTransformation())
                    .transform(RoundedCornersTransformation(100, 0))
                    .placeholder(R.drawable.loading_icon).into(recipeImageView)
            }.addOnFailureListener {
                recipeImageView.setImageResource(R.drawable.no_image_icon)
            }
        }
        return recipeData.name
    }
}