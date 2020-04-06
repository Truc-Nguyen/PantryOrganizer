package com.example.pantry_organizer.home.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.PantryData
import com.example.pantry_organizer.global.fragment.AbstractPantryAppFragment
import com.example.pantry_organizer.home.adapters.PantryDataAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.dialog_add_pantry.*
import kotlinx.android.synthetic.main.fragment_food_list.*
import kotlinx.android.synthetic.main.fragment_food_list.view.*
import kotlinx.android.synthetic.main.fragment_pantry_list.*

class PantryFoodFragment: AbstractPantryAppFragment() {
    private var foodRecycler: RecyclerView? = null
    private var mAdapter: PantryDataAdapter? = null

    // firebase instances already set in AbstractPantryAppFragment.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_food_list, container, false)

    }

    override fun onStart(savedInstanceState: Bundle?) {
        super.onStart()


        foodRecycler = view!!.findViewById(R.id.food_recycler_view)

        // Get users sorted by chips
        val mQuery = db.collection("names")

        // RecyclerView
        mAdapter = object : PantryDataAdapter(mQuery) {
        }

        foodRecycler!!.setLayoutManager(LinearLayoutManager(this))
        foodRecycler!!.setAdapter(mAdapter)

        Log.d("recycler","set up w/ query ")

        add_food_button.setOnClickListener{
            //function to add new food item to database
        }

        back_button.setOnClickListener{
            //go back to list of pantries
        }

    }

}