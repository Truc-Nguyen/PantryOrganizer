package com.example.pantry_organizer.home.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.pantry_organizer.R
import com.example.pantry_organizer.global.activity.AbstractPantryAppActivity
import com.example.pantry_organizer.pantry.activity.AddPantryActivity
import com.example.pantry_organizer.pantry.fragment.PantryListFragment
import com.example.pantry_organizer.planner.fragment.PlanningListFragment
import com.example.pantry_organizer.recipe.fragment.AddRecipeActivity
import com.example.pantry_organizer.recipe.fragment.RecipeListFragment
import com.example.pantry_organizer.shopping.fragment.ShoppingListFragment
import com.example.pantry_organizer.userManagement.activity.UserManagementActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.dialog_sign_out.*

class HomeActivity: AbstractPantryAppActivity() {
    // App menu identifier.
    private var menuID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize fragment on home activity to pantry fragment.
        swapFragment(resources.getString(R.string.pantry_nav), PantryListFragment(), R.menu.add_pantry_menu)

        // Define navigation menu button listeners.
        appNav_appBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.pantry_navMenu -> swapFragment(resources.getString(R.string.pantry_nav), PantryListFragment(), R.menu.add_pantry_menu)
                R.id.recipe_navMenu -> swapFragment(resources.getString(R.string.recipe_nav), RecipeListFragment(), R.menu.add_recipe_menu)
                R.id.planner_navMenu -> swapFragment(resources.getString(R.string.planner_nav), PlanningListFragment(), null)
                R.id.shopping_navMenu -> swapFragment(resources.getString(R.string.shopping_nav), ShoppingListFragment(), null)
            }
            true
        }
    }

    // Swap out a fragment on the home activity.
    private fun swapFragment(title: String, fragment: Fragment, menu: Int?) {
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
        // Destroy the current app menu.
        menu?.clear()
        // todo remove this when all fragments are implemented and change menu to non-nullable
        if (menuID != null) {
            // Inflate the new menu.
            menuInflater.inflate(menuID!!, menu)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    // App menu item listeners.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addPantry_menuItem -> {
                startActivity(Intent(this, AddPantryActivity:: class.java))
                true
            }
            R.id.addRecipe_menuItem -> {
                startActivity(Intent(this, AddRecipeActivity:: class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Prompt user for sign out when the back button is pressed.
    override fun onBackPressed() {
        signOut()
    }

    // Sign out of user account and return to user management page.
    private fun signOut() {
        // Build an alert dialog for confirmation out.
        val signOutView = LayoutInflater.from(this).inflate(R.layout.dialog_sign_out,null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(signOutView)
        val dialog = dialogBuilder.show()

        // User confirms sign out.
        dialog.signOutConfirm_button.setOnClickListener{
            dialog.dismiss()
            FirebaseAuth.getInstance().signOut()

            // Return to main menu.
            val intent = Intent(this, UserManagementActivity::class.java)
            startActivity(intent)
        }

        // User selects cancel.
        dialog.signOutCancel_button.setOnClickListener {
            dialog.dismiss()
        }
    }
}