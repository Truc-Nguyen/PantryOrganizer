package com.example.pantry_organizer.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pantry_organizer.R
import com.example.pantry_organizer.global.fragment.AbstractPantryAppFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PantryFoodFragment: AbstractPantryAppFragment() {
    // firebase instances already set in AbstractPantryAppFragment.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_food_list, container, false)

    }

}