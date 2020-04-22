package com.example.pantry_organizer.shopping.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.ShoppingData

class ShoppingListAdapter(private val list: ArrayList<ShoppingData>?): RecyclerView.Adapter<ShoppingListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ShoppingListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        // Extract the pantry data from the position in the list.
        val shoppingData = list!![position]

        // Bind pantry data at this position to recycler view item.
        holder.bind(shoppingData)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }
}

class ShoppingListViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_shopping_list_item, parent, false)) {
    fun bind(shoppingData: ShoppingData?) {
        // Get view objects.
        val shoppingNameView: TextView = itemView.findViewById(R.id.adapter_shoppingName_textView)
        val shoppingQuantityView: TextView = itemView.findViewById(R.id.adapter_shoppingQuantity_textView)

        // Update view object data.
        shoppingNameView.text = shoppingData?.name
        shoppingQuantityView.text = shoppingData?.quantity.toString()
    }
}