package com.example.pantry_organizer.pantry.activity

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.example.pantry_organizer.R
import com.example.pantry_organizer.global.activity.AbstractPantryAppActivity
import com.example.pantry_organizer.global.adapter.ViewPagerAdapter
import com.example.pantry_organizer.pantry.fragment.AddCustomFoodFragment
import com.example.pantry_organizer.pantry.fragment.AddOnlineFoodFragment
import kotlinx.android.synthetic.main.activity_add_food.*

class AddFoodActivity: AbstractPantryAppActivity() {
    private lateinit var pantryName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        // Extract the extras from intent.
        pantryName = intent.extras!!.getString("pantryName")!!

        // Support bar attributes.
        supportActionBar?.title = "Stock Pantry"
        supportActionBar?.subtitle = pantryName
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize online search fragment to the activity frame layout.
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.addFood_frameLayout, AddOnlineFoodFragment())
        transaction.commit()
    }

    // Inflate custom food activity app bar menu.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_custom_food_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Return to previous activity.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}