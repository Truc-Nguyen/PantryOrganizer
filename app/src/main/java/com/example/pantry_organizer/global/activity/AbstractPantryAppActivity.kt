package com.example.pantry_organizer.global.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.pantry_organizer.global.viewModel.AppViewModel

abstract class AbstractPantryAppActivity: AppCompatActivity() {
    // Define the global view model.
    lateinit var viewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instantiate the global view model.
        viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
    }
}