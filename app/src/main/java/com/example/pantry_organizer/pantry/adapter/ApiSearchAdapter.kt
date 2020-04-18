package com.example.pantry_organizer.pantry.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.ApiFoodData
import com.example.pantry_organizer.global.viewModel.AppViewModel
import com.example.pantry_organizer.pantry.activity.AddFoodActivity
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

class ApiSearchAdapter(private val list: ArrayList<ApiFoodData>?, private val pantryName: String)
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
            intent.putExtra("pantryName", pantryName)
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


// todo old info

//        holder.itemView.setOnClickListener(object: View.OnClickListener {
//            override fun onClick(view: View?) {
//                Log.d("Into On click", "We go")
//                val builder = AlertDialog.Builder(context).create()
//                val inflater = holder.savedInflater
//                val dialogLayout = inflater.inflate(R.layout.alert_dialog_add_online_food, null)
//                val amount = dialogLayout.findViewById<EditText>(R.id.online_food_amount)
//                builder.setView(dialogLayout)
//                val cancelButton = dialogLayout.findViewById<Button>(R.id.online_food_amount_cancel_button)
//                val confirmButton = dialogLayout.findViewById<Button>(R.id.online_food_amount_confirm_button)
//
//                builder.show()
//                Log.d("Into On click", "After show")
//
//                cancelButton.setOnClickListener {
//                    val myToast = Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT)
//                    myToast.show()
//                    builder.dismiss()
//                }

//                confirmButton.setOnClickListener {
//                    val myToast = Toast.makeText(context, "Added " + amount.text.toString() + " of item " + foodName + " to pantry", Toast.LENGTH_SHORT)
//                    //call function to add this food to pantry
//                    Log.d("Food Query Sent", "Sent")
//                    viewModel.getFoodNutrients(foodName)
//                    viewModel!!.foodNutrients.observe(owner, Observer {
//                        Log.d("Food Query Status", "test")
//                        val queryResults = viewModel.foodNutrients.value!!.foods
//                        val foodAmount = amount.text.toString()
//                        if (queryResults.isNotEmpty()) {
//                            // Create new food database entry
//                            val bundle = fragment.arguments
//                            val currentPantry = bundle!!.getString("EnterPantry","none")
//                            val foodData = FireBaseFood(queryResults[0])
//                            val foodNameAndAmount: String = "$foodName,$foodAmount"
//                            // Attempt attempt to add the food to the data base
//                            //Will need to add code to check whether food already exists in database and update the amount if it does
//                            if (viewModel.addFoodToFirebase(foodData.getDataMap())) {
//                                // Push successful.
//                                Toast.makeText(fragment.context, "$foodName added to firebase", Toast.LENGTH_LONG).show()
////                                if (viewModel.addFoodToPantry(currentPantry,foodNameAndAmount)){
////                                    Toast.makeText(fragment.context, "$foodName added to pantry food list", Toast.LENGTH_LONG).show()
////                                }
//                            }
//
//
//                            Log.d("Food Query Status", queryResults.toString())
//                            // add to firebase along with amount
//                        } else {
//                            Log.d("Food Query Status", "Unsuccessful")
//                        }
//                        myToast.show()
//                    })
//                        builder.dismiss()
//                }
//            }
//        })