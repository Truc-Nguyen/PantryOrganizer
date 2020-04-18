package com.example.pantry_organizer.pantry.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.ApiFoodPreview
import com.example.pantry_organizer.global.viewModel.ViewModel
import com.squareup.picasso.Picasso

//interface OnFoodItemClickListener{
//    fun onFoodItemClicked(food: ApiFoodPreview)
//}

class FoodPreviewAdapter(private val list: ArrayList<ApiFoodPreview>?, private val viewModel: ViewModel, private val owner: LifecycleOwner): RecyclerView.Adapter<FoodPreviewViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodPreviewViewHolder {
        val inflater = LayoutInflater.from(parent.context) //        inflater
        return FoodPreviewViewHolder(inflater, parent)
    }

    //bind the object
    override fun onBindViewHolder(holder: FoodPreviewViewHolder, position: Int) {
        val foodName = list!![position].food_name
        val context = holder.savedParent.context

        holder.bind(list!![position])
        holder.itemView.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View?) {
                Log.d("Into On click", "We go")
                val builder = AlertDialog.Builder(context).create()
                val inflater = holder.savedInflater
                val dialogLayout = inflater.inflate(R.layout.alert_dialog_add_online_food, null)
                val amount = dialogLayout.findViewById<EditText>(R.id.online_food_amount)
                builder.setView(dialogLayout)
                val cancelButton = dialogLayout.findViewById<Button>(R.id.online_food_amount_cancel_button)
                val confirmButton = dialogLayout.findViewById<Button>(R.id.online_food_amount_confirm_button)

                builder.show()
                Log.d("Into On click", "After show")

                cancelButton.setOnClickListener {
                    val myToast = Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT)
                    myToast.show()
                    builder.dismiss()
                }

                confirmButton.setOnClickListener {
                    val myToast = Toast.makeText(
                        context,
                        "Added " + amount.text.toString() + " of item " + foodName + " to pantry",
                        Toast.LENGTH_SHORT
                    )
                    //call function to add this food to pantry
                    Log.d("Food Query Sent", "Sent")
                    viewModel.getFoodNutrientsFromApi(foodName)
                    viewModel!!.apiFoodNutrients.observe(owner, Observer {
                        Log.d("Food Query Status", "test")
                        val queryResults = viewModel.apiFoodNutrients.value!!.foods
                        if (queryResults.isNotEmpty()) {
                            Log.d("Food Query Status", queryResults.toString())
                            // add to firebase along with amount
                        } else {
                            Log.d("Food Query Status", "Unsuccessful")
                        }
                        myToast.show()
                    })
                        builder.dismiss()
                }
            }
        })
    //set the count
    }
    override fun getItemCount(): Int = list!!.size
}



//create the view holder
class FoodPreviewViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_online_food_preview_list_item, parent, false)) {
    val savedParent = parent
    val savedInflater = inflater

    fun bind(food: ApiFoodPreview) {
        val food_name: TextView = itemView.findViewById(R.id.food_preview_name)
        val food_img: ImageView = itemView.findViewById(R.id.food_preview_picture)
        food_name.text = food!!.food_name
        val picasso = Picasso.get()
//        val trackImgUrl = food.photo.thumb
        val trackImgUrl = food.photo.thumb
        picasso.load(trackImgUrl).into(food_img)
    }
}

//create the listener for the recycler view position: Int



