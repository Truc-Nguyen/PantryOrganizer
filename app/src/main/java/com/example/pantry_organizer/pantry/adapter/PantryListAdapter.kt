package com.example.pantry_organizer.pantry.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.PantryData
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
        holder.bind(list!![position])
    }

    override fun getItemCount(): Int {
        return list!!.size
    }
}

class PantryListViewHolder(inflater: LayoutInflater, parent: ViewGroup):
RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_pantry_list_item, parent, false)) {
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