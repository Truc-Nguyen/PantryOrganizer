package com.example.pantry_organizer.global.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.pantry_organizer.global.viewModel.ViewModel

abstract class AbstractPantryAppActivity: AppCompatActivity() {
    // Define the global view model.
    lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instantiate the global view model.
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
    }
}