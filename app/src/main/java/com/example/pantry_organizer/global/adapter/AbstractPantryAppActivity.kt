package com.example.pantry_organizer.global.adapter

import android.content.Intent
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.pantry_organizer.R
import com.example.pantry_organizer.userManagement.activity.UserManagementActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.dialog_logout.*

abstract class AbstractPantryAppActivity: AppCompatActivity() {
    // Set firebase instances.
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onBackPressed() {
        logout()
    }

    // Log out and return to user management page.
    private fun logout() {
        // Build an alert dialog for logging out.
        val logoutView = LayoutInflater.from(this).inflate(R.layout.dialog_logout, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(logoutView)
            .setTitle("Logout")
        val dialog = dialogBuilder.show()

        dialog.logoutYes_button.setOnClickListener{
            dialog.dismiss()
            auth.signOut()

            // Return to main menu.
            val intent = Intent(this, UserManagementActivity::class.java)
            startActivity(intent)
        }

        dialog.logoutNo_button.setOnClickListener {
            dialog.dismiss()
        }
    }
}