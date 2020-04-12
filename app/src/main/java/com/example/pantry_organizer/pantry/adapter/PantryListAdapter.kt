package com.example.pantry_organizer.pantry.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.PantryData
import com.example.pantry_organizer.pantry.fragment.PantryFoodFragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation


class PantryListAdapter(private val list: ArrayList<PantryData>?): RecyclerView.Adapter<PantryListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PantryListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PantryListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PantryListViewHolder, position: Int) {
        val pantryName = list!![position].name

        holder.bind(list!![position])
        holder.itemView.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View?){
                //            val myToast =
//                Toast.makeText(this.context,"This would take you to food in the pantry", Toast.LENGTH_SHORT)
//            myToast.show()
                val activity = view!!.context as AppCompatActivity
                var bundle = Bundle()
                //is there a better identifier than pantry name?
                Log.d("PantryName", pantryName)
                bundle.putString("pantry_name", pantryName)
                val fragment = PantryFoodFragment()
                fragment.arguments = bundle
                val transanction = activity.supportFragmentManager!!.beginTransaction()
                transanction.replace(R.id.home_frameLayout, fragment)
                transanction.commit()
            }
        })

    }

    override fun getItemCount(): Int {
        return list!!.size
    }
}

class PantryListViewHolder(inflater: LayoutInflater, parent: ViewGroup):
RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_pantry_list_item, parent, false)) {
    //added to allow toasts to be made for testing in the bind function
    private val context = parent.context

    fun bind(pantryData: PantryData) {
        val pantryNameView: TextView = itemView.findViewById(R.id.adapter_pantryList_pantryName_textView)
        val pantryLocationView: TextView = itemView.findViewById(R.id.adapter_pantryList_pantryLocation_textView)
        val pantryImageView: ImageView = itemView.findViewById(R.id.adapter_pantryList_pantryImage_imageView)

        pantryNameView.text = pantryData.name
        pantryLocationView.text = pantryData.location
        if (pantryData.imageLink == null) {
            pantryImageView.setImageResource(R.drawable.no_image_icon)
        } else {
            val imageRef = Firebase.storage.reference.child(pantryData.imageLink)

            imageRef.downloadUrl.addOnSuccessListener {
                Picasso.get()
                    .load(it)
                    .transform(CropSquareTransformation())
                    .transform(RoundedCornersTransformation(100, 0))
                    .placeholder(R.drawable.loading_icon).into(pantryImageView)
            }.addOnFailureListener {
                pantryImageView.setImageResource(R.drawable.no_image_icon)
            }
        }
    }
}