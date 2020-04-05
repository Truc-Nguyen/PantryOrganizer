package com.example.pantry_organizer.global.activity

import android.content.Intent
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.pantry_organizer.R
import com.example.pantry_organizer.userManagement.activity.UserManagementActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.dialog_logout.*

abstract class AbstractPantryAppActivity: AppCompatActivity() {
    // Set firebase instances.
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val fbs = Firebase.storage.reference
    val userID = auth.currentUser?.uid

    // Log out and return to user management page.
    fun logout() {
        // Build an alert dialog for logging out.
        val logoutView = LayoutInflater.from(this).inflate(R.layout.dialog_logout, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(logoutView)
            .setTitle("Logout")
        val dialog = dialogBuilder.show()

        // User selects yes to logout.
        dialog.logoutYes_button.setOnClickListener{
            dialog.dismiss()
            auth.signOut()

            // Return to main menu.
            val intent = Intent(this, UserManagementActivity::class.java)
            startActivity(intent)
        }

        // User selects no to cancel.
        dialog.logoutNo_button.setOnClickListener {
            dialog.dismiss()
        }
    }
}