package com.example.pantry_organizer.home.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.ShoppingData
import com.example.pantry_organizer.global.activity.AbstractPantryAppActivity
import com.example.pantry_organizer.pantry.activity.AddPantryActivity
import com.example.pantry_organizer.pantry.fragment.PantryListFragment
import com.example.pantry_organizer.planner.fragment.fragment.PlanningListFragment
import com.example.pantry_organizer.planner.fragment.fragment.ShoppingListFragment
import com.example.pantry_organizer.recipe.activity.AddRecipeActivity
import com.example.pantry_organizer.recipe.fragment.RecipeListFragment
import com.example.pantry_organizer.userManagement.activity.UserManagementActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.dialog_add_item_to_shopping.*
import kotlinx.android.synthetic.main.dialog_confirm_remove_food.*
import kotlinx.android.synthetic.main.dialog_sign_out.*

class HomeActivity: AbstractPantryAppActivity() {
    // App menu identifier.
    private var menuID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize fragment on home activity to pantry fragment.
        swapFragment(resources.getString(R.string.pantry_nav), PantryListFragment(), R.menu.add_pantry_menu)

//         Define navigation menu button listeners.
        appNav_appBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.pantry_navMenu -> swapFragment(resources.getString(R.string.pantry_nav), PantryListFragment(), R.menu.add_pantry_menu)
                R.id.recipe_navMenu -> swapFragment(resources.getString(R.string.recipe_nav), RecipeListFragment(), R.menu.add_recipe_menu)
                R.id.planner_navMenu -> swapFragment(resources.getString(R.string.planner_nav), PlanningListFragment(), R.menu.add_planning_menu)
                R.id.shopping_navMenu -> swapFragment(resources.getString(R.string.shopping_nav), ShoppingListFragment(), R.menu.add_shopping_menu)
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
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addPantry_menuItem -> {
                startActivity(Intent(this, AddPantryActivity:: class.java))
                true
            }
            R.id.addRecipe_menuItem -> {
                startActivity(Intent(this, AddRecipeActivity::class.java))
                true
            }
            R.id.addMeal_menuItem -> {
                Log.d("Test", "addMeal")
                true
            }
            R.id.addItem_menuItem -> {
                Log.d("Test", "addShopping")
                val removeFoodQuantityConfirmDialog = LayoutInflater.from(this).inflate(
                    R.layout.dialog_add_item_to_shopping, null)
                val dialogBuilder = android.app.AlertDialog.Builder(this)
                    .setView(removeFoodQuantityConfirmDialog)
                val dialog = dialogBuilder.show()

                // Update the remove food message.
                val messageView: TextView = dialog.findViewById(R.id.addItemMessage_textView)
                val message = "Enter the food name and quantity to add to the list"
                messageView.text = message

//                 User confirms addition.
                dialog.addItemConfirm_button.setOnClickListener{
                    // Define views.
                    val qtyView: EditText = dialog.findViewById(R.id.addItemQuantity_editText)
                    val nameView: EditText = dialog.findViewById(R.id.addItemName_editText)
                    val confirmButton: Button = dialog.findViewById(R.id.addItemConfirm_button)
                    val cancelButton: Button = dialog.findViewById(R.id.addItemCancel_button)

                    // Reset quantity field color.
                    qtyView.background = resources.getDrawable(R.drawable.edit_text_border, null)

                    // Sanitize input.
                    val qtyInput = qtyView.text.toString()
                    val nameInput = nameView.text.toString()
                    if (qtyInput == "" || nameInput == "") {
                        Toast.makeText(this, "Please enter a valid quantity and name.", Toast.LENGTH_LONG).show()
                        qtyView.background = resources.getDrawable(R.drawable.edit_text_border_red, null)
                        return@setOnClickListener
                    }

                    // Extract the quantity of item to add.
                    val item = ShoppingData(nameInput, qtyInput.toLong()).getDataMap()

                    // Disable the buttons once a request has been made.
                    confirmButton.isEnabled = false
                    cancelButton.isEnabled = false

                    // Delete the selected pantry.
                    viewModel.addShoppingListItem(item)

                    dialog.dismiss()
                }

                // User selects cancel.
                dialog.addItemCancel_button.setOnClickListener {
                    dialog.dismiss()
                }
                true
            }else -> super.onOptionsItemSelected(item)
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