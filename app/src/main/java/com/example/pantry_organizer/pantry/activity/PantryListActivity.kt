package com.example.pantry_organizer.pantry.activity

import android.os.Bundle
import com.example.pantry_organizer.R
import com.example.pantry_organizer.global.adapter.AbstractPantryAppActivity

class PantryListActivity: AbstractPantryAppActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantry_list)
    }
}
