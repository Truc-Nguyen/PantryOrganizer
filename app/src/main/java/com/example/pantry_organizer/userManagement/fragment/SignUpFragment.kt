package com.example.pantry_organizer.userManagement.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.UserData
import com.example.pantry_organizer.pantry.activity.PantryListActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment(private val db: FirebaseFirestore, private val auth: FirebaseAuth) : Fragment() {
    // Toggle components enable flag.
    private var enableComponents: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sign up button click listener.
        signUp_button.setOnClickListener {
            toggleEnabledFields()

            // Harvest user input.
            val email = signUpEmail_editText.text.toString()
            val password = signUpPassword_editText.text.toString()
            val confirmPassword = signUpConfirmPassword_editText.text.toString()
            val emailRegex = "^\\w+@\\w+[.][a-zA-z]{2,5}$".toRegex()
            signUpPassword_editText.text.clear()
            signUpConfirmPassword_editText.text.clear()

            // Sanitize input.
            if (email == "") {
                toggleEnabledFields()
                Toast.makeText(activity, "Email cannot be blank.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else if (!emailRegex.matches(email)) {
                toggleEnabledFields()
                Toast.makeText(activity, "Invalid email format.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else if (password.length < 6) {
                toggleEnabledFields()
                Toast.makeText(
                    activity,
                    "Password must be at least 6 characters.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            } else if (password != confirmPassword) {
                toggleEnabledFields()
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
                        toggleEnabledFields()
                        Toast.makeText(activity, "This email address has already been registered.", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    // Toggle enabling of user input components while waiting for user authentication.
    private fun toggleEnabledFields() {
        // Toggle components.
        enableComponents = !enableComponents
        signUp_button.isEnabled = enableComponents
        signUpEmail_editText.isEnabled = enableComponents
        signUpPassword_editText.isEnabled = enableComponents
        signUpConfirmPassword_editText.isEnabled = enableComponents

        // Toggle component text.
        if (enableComponents) {
            signUp_button.text = resources.getString(R.string.sign_up_fragment_title)
        } else {
            signUp_button.text = resources.getString(R.string.sign_up_fragment_status)
        }
    }
}