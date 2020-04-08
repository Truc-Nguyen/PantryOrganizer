package com.example.pantry_organizer.home.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.pantry_organizer.R
import com.example.pantry_organizer.global.activity.AbstractPantryAppActivity
import com.example.pantry_organizer.global.adapter.ViewPagerAdapter
import com.example.pantry_organizer.pantry.activity.AddPantryActivity
import com.example.pantry_organizer.pantry.fragment.PantryFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity: AbstractPantryAppActivity() {
    // App menu identifier.
    private var menuID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize fragment on home activity to pantry fragment.
        swapFragment(resources.getString(R.string.pantry_nav), PantryFragment(), R.menu.add_pantry_menu)

        // Define navigation menu button listeners.
        appNav_appBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.pantry_navMenu -> swapFragment(resources.getString(R.string.pantry_nav), PantryFragment(), R.menu.add_pantry_menu)
                //R.id.recipe_navMenu -> swapFragment(resources.getString(R.string.recipe_nav), )
                //R.id.planner_navMenu -> swapFragment(resources.getString(R.string.planner_nav), )
                //R.id.shopping_navMenu -> swapFragment(resources.getString(R.string.shopping_nav), )
            }
            true
        }
    }

    // Swap out a fragment on the home activity.
    private fun swapFragment(title: String, fragment: Fragment, menu: Int) {
        if (menuID != menu) {
            // Update the activity title bar.
            supportActionBar?.title = title

            // Update the activity app menu.
            menuID = menu
            invalidateOptionsMenu()

            // Swap in the fragment.
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.home_frameLayout, fragment)
            transaction.commit()
        }
    }

    // Inflate the app menu corresponding to the current menuID.
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(menuID, menu)
        return super.onPrepareOptionsMenu(menu)
    }

    // App menu item listeners.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addPantry_menuItem -> {
                startActivity(Intent(this, AddPantryActivity:: class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Prompt user for logout when the back button is pressed.
    override fun onBackPressed() {
        logout()
    }
}