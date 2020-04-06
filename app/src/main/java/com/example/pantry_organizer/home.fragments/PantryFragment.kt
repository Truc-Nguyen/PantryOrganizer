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
import com.example.pantry_organizer.global.fragment.AbstractPantryAppFragment
import com.example.pantry_organizer.home.adapters.PantryDataAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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

        pantryRecycler =findViewById(R.id.pantry_recycler_view)

        // Get users sorted by chips
        val mQuery = db.collection("names")

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
        val dialogView = LayoutInflater.from(this.context).inflate(R.layout.enter_ace_value, null)
        val mBuilder = android.app.AlertDialog.Builder(this.context)
            .setView(dialogView)
            .setTitle("Enter ace value: 1 or 11")
            .setCancelable(false)
        val mAlertDialog = mBuilder.show()

        // Sets an onclick listener on the dialog box button
        mAlertDialog.submitValue.setOnClickListener {
            val inputValue = dialogView.ace_value.text.toString().toInt()
            if(inputValue == 1 || inputValue == 11){
                // updating player total here so that it correctly updates based on the chosen value
                playerTotal+=inputValue
                Log.d("playerTotal", playerTotal.toString())
                //check if player busts
                if (playerTotal>21) {
                    endGame()
                }
                mAlertDialog.dismiss()
            }else{
                Toast.makeText(
                    this, "You must input either 1 or 11",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}