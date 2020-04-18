package com.example.pantry_organizer.pantry.adapter

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.ApiFoodPreview
import com.example.pantry_organizer.global.viewModel.ViewModel
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation




interface OnFoodItemClickListener{
    fun onFoodItemClicked(food: ApiFoodPreview)
}

class FoodPreviewAdapter(private val list: ArrayList<ApiFoodPreview>?, val viewModel: ViewModel): RecyclerView.Adapter<FoodPreviewViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodPreviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FoodPreviewViewHolder(inflater, parent)
    }

    //bind the object
    override fun onBindViewHolder(holder: FoodPreviewViewHolder, position: Int) {
        val foodName = list!![position].food_name
        val context = holder.savedParent.context

        holder.bind(list!![position])
        holder.itemView.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View?){
                val builder = AlertDialog.Builder(context).create()
                val inflater = holder.savedInflater
                val dialogLayout = inflater.inflate(R.layout.alert_dialog_add_online_food, null)
                val amount  = dialogLayout.findViewById<EditText>(R.id.online_food_amount)
                builder.setView(dialogLayout)
//                val cancel_button: Button = dialogLayout.findViewById(R.id.online_food_amount_cancel_button)
//                val confirm_button: ImageView = dialogLayout.findViewById(R.id.online_food_amount_confirm_button)
                val cancel_button = dialogLayout.findViewById<Button>(R.id.online_food_amount_cancel_button)
                val confirm_button = dialogLayout.findViewById<Button>(R.id.online_food_amount_confirm_button)

//                builder.setNegativeButton("Cancel") { dialogInterface, i -> Toast.makeText(context, "Negative is " + amount.text.toString(), Toast.LENGTH_SHORT).show() }
//                builder.setPositiveButton("Add Food to Pantry") { dialogInterface, i -> Toast.makeText(context, "Positive is " + amount.text.toString(), Toast.LENGTH_SHORT).show() }

                cancel_button.setOnClickListener {
                    val myToast = Toast.makeText(context,"Canceled", Toast.LENGTH_SHORT)
                    myToast.show()
                    builder.dismiss()
                }

                confirm_button.setOnClickListener {

                    viewModel.getFoodNutrients(list[position].tag_name)

                    val myToast = Toast.makeText(context,"Added " + amount.text.toString() +" of item " + foodName + " to pantry", Toast.LENGTH_SHORT)
                    //call function to add this food to pantry
                    myToast.show()
                    builder.dismiss()
                }
                builder.show()
            }
        })
    }

    //set the count
    override fun getItemCount(): Int = list!!.size
}


//create the view holder
//class FoodPreviewViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
//    RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_food_list_item, parent, false)) {

class FoodPreviewViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_online_food_preview_list_item, parent, false)) {
    val savedParent = parent
    val savedInflater = inflater

    fun bind(food: ApiFoodPreview) {

        val food_name: TextView = itemView.findViewById(R.id.food_preview_name)
        val food_img: ImageView = itemView.findViewById(R.id.food_preview_picture)
//        val food_name: TextView = itemView.findViewById(R.id.adapter_pantryList_pantryLocation_textView)
//        val food_img: ImageView = itemView.findViewById(R.id.adapter_pantryList_pantryImage_imageView)
        food_name.text = food!!.food_name
        val picasso = Picasso.get()
//        val trackImgUrl = food.photo.thumb
//        picasso.load(trackImgUrl).into(food_img)
    }
}

//create the listener for the recycler view position: Int



