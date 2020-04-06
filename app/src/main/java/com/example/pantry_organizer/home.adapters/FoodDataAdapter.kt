package com.example.pantry_organizer.home.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.FoodData
import com.example.pantry_organizer.data.PantryData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_food_item.view.*
import kotlinx.android.synthetic.main.fragment_pantry_item.view.*

open class FoodDataAdapter(query: Query) :
    FirestoreAdapter<FoodDataAdapter.ViewHolder>(query) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.fragment_food_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position), position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            snapshot: DocumentSnapshot,
            pos: Int
        ) {
            val food = snapshot.toObject(FoodData::class.java)
            itemView.pantry_food_name.text = food!!.name
            Picasso.get().load(food!!.imageLink).into(itemView.food_imageView);
        }
    }
}
