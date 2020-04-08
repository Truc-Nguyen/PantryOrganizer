package com.example.pantry_organizer.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.PantryData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_pantry_item.view.*

interface OnPantryItemClickListener{
    fun onPantryItemClicked(pantry: PantryData)
}

open class PantryDataAdapter(query: Query, val itemClickListener: OnPantryItemClickListener) :
    FirestoreAdapter<PantryDataAdapter.ViewHolder>(query) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ViewHolder(inflater.inflate(R.layout.fragment_pantry_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position), itemClickListener)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(snapshot: DocumentSnapshot, clickListener: OnPantryItemClickListener) {
            val pantry = snapshot.toObject(PantryData::class.java)
            itemView.pantry_text.text = pantry!!.name

            itemView.setOnClickListener {
                clickListener.onPantryItemClicked(pantry)

            }
        }
    }
}


