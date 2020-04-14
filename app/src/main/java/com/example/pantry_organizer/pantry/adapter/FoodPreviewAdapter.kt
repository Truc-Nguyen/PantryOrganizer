package com.example.pantry_organizer.pantry.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.ApiFoodPreview
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

interface OnFoodItemClickListener{
    fun onFoodItemClicked(food: ApiFoodPreview)
}

//create the view holder
//class FoodPreviewViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
//    RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_food_list_item, parent, false)) {

class FoodPreviewViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_online_food_preview_list_item, parent, false)) {

    fun bind(food: ApiFoodPreview) {

        val food_name: TextView = itemView.findViewById(R.id.food_preview_name)
        val food_img: ImageView = itemView.findViewById(R.id.food_preview_picture)
//        val food_name: TextView = itemView.findViewById(R.id.adapter_pantryList_pantryLocation_textView)
//        val food_img: ImageView = itemView.findViewById(R.id.adapter_pantryList_pantryImage_imageView)
        food_name.text = food!!.food_name
        val picasso = Picasso.get()
        val trackImgUrl = food.photo.thumb
        picasso.load(trackImgUrl).into(food_img)
    }
}

//create the listener for the recycler view position: Int
class FoodPreviewAdapter(private val list: ArrayList<ApiFoodPreview>?): RecyclerView.Adapter<FoodPreviewViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodPreviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FoodPreviewViewHolder(inflater, parent)
    }

    //bind the object
    override fun onBindViewHolder(holder: FoodPreviewViewHolder, position: Int) {
        val pantryName = list!![position].food_name

        holder.bind(list!![position])
        holder.itemView.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View?){
//                //            val myToast =
////                Toast.makeText(this.context,"This would take you to food in the pantry", Toast.LENGTH_SHORT)
////            myToast.show()
//                val activity = view!!.context as AppCompatActivity
//                var bundle = Bundle()
//                //is there a better identifier than pantry name?
//                Log.d("PantryName", pantryName)
//                bundle.putString("pantry_name", pantryName)
//                val fragment = PantryFoodFragment()
//                fragment.arguments = bundle
//                val transanction = activity.supportFragmentManager!!.beginTransaction()
//                transanction.replace(R.id.home_frameLayout, fragment)
//                transanction.commit()
            }
        })
    }

    //set the count
    override fun getItemCount(): Int = list!!.size
}


