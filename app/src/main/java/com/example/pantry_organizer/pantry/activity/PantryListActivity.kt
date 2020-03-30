package com.example.pantry_organizer.pantry.activity

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.pantry_organizer.R

import kotlinx.android.synthetic.main.activity_pantry_list.*

class PantryListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantry_list)
    }
}
