package com.example.pantry_organizer.userManagement.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pantry_organizer.R
import com.example.pantry_organizer.pantry.activity.PantryListActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment(private val db: FirebaseFirestore, private val auth: FirebaseAuth) : UserManagementFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sign up button click listener.
        signUp_button.setOnClickListener {
            toggleEnabledComponents()

            // Harvest user input.
            val email = signUpEmail_editText.text.toString()
            val password = signUpPassword_editText.text.toString()
            val confirmPassword = signUpConfirmPassword_editText.text.toString()
            val emailRegex = "^\\w+@\\w+[.][a-zA-z]{2,5}$".toRegex()
            signUpPassword_editText.text.clear()
            signUpConfirmPassword_editText.text.clear()

            // Sanitize input.
            if (email == "") {
                toggleEnabledComponents()
                Toast.makeText(activity, "Email cannot be blank.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else if (!emailRegex.matches(email)) {
                toggleEnabledComponents()
                Toast.makeText(activity, "Invalid email format.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else if (password.length < 6) {
                toggleEnabledComponents()
                Toast.makeText(
                    activity,
                    "Password must be at least 6 characters.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            } else if (password != confirmPassword) {
                toggleEnabledComponents()
                Toast.makeText(
                    activity,
                    "Passwords do not match.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // Attempt to sign up with given credentials.
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Start the pantry list activity for this user.
                        val intent = Intent(activity, PantryListActivity::class.java)
                        activity!!.startActivity(intent)
                    } else {
                        toggleEnabledComponents()
                        Toast.makeText(activity, "This email address has already been registered.", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}