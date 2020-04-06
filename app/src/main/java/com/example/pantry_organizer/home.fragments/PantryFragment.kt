package com.example.pantry_organizer.home.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.dialog_add_pantry.*
import kotlinx.android.synthetic.main.fragment_food_list.view.*
import kotlinx.android.synthetic.main.fragment_pantry_list.*

class PantryFragment: AbstractPantryAppFragment() {
    // firebase instances already set in AbstractPantryAppFragment.

    var settings: FirebaseFirestoreSettings
    private var pantryRecycler: RecyclerView? = null
    private var mAdapter: PantryDataAdapter? = null

    init{
        settings = FirebaseFirestoreSettings.Builder()
            .build()
        db.setFirestoreSettings(settings)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_pantry_list, container, false)



    }

    override fun onStart(savedInstanceState: Bundle?) {
        super.onStart()


        pantryRecycler = view!!.findViewById(R.id.pantry_recycler_view)

        // Get users sorted by chips
        val mQuery = db.collection("pantry_names")

        // RecyclerView
        mAdapter = object : PantryDataAdapter(mQuery) {
        }

        pantryRecycler!!.setLayoutManager(LinearLayoutManager(this))
        pantryRecycler!!.setAdapter(mAdapter)

        Log.d("recycler","set up w/ query ")

        add_pantry_button.setOnClickListener{
            addPantryDialog()
        }

    }

    private fun addPantryDialog() {
        // Opens the dialog view asking the user for
        val dialogView = LayoutInflater.from(this.context).inflate(R.layout.dialog_add_pantry, null)
        val mBuilder = android.app.AlertDialog.Builder(this.context)
            .setView(dialogView)
            .setTitle("Enter new food location")
            .setCancelable(true)
        val mAlertDialog = mBuilder.show()

        // Sets an onclick listener on the dialog box button
        mAlertDialog.add_to_pantry.setOnClickListener {
            val newLocation = dialogView.pantry_name.text.toString()

            //need to check if pantry location already exists?
            if(newLocation != ""){
                //create new pantry object
                val location = PantryData(newLocation)
                // add new pantry location
                db.collection("pantry_names").document(newLocation)
                    .set(location)

                mAlertDialog.dismiss()
            }else{
                Toast.makeText(
                    this.context, "Please enter pantry location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}