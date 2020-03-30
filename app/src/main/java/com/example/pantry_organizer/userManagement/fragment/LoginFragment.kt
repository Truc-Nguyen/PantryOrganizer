package com.example.pantry_organizer.userManagement.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.pantry_organizer.R
import com.example.pantry_organizer.pantry.activity.PantryListActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment(private val db: FirebaseFirestore, private val auth: FirebaseAuth): UserManagementFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sign up button click listener.
        signUp_navigate.setOnClickListener {
            val viewPager: ViewPager = activity!!.findViewById(R.id.userManagement_viewPager)
            viewPager.currentItem = 1
        }

        // Login button click listener.
        login_button.setOnClickListener {
            toggleEnabledComponents()

            // Harvest user input.
            val email = loginEmail_editText.text.toString()
            val password = loginPassword_editText.text.toString()
            val emailRegex = "^\\w+@\\w+[.][a-zA-z]{2,5}$".toRegex()
            loginPassword_editText.text.clear()

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
                Toast.makeText(activity, "Password must be at least 6 characters.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Attempt user login with given credentials.
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Start the pantry list activity for this user.
                        val intent = Intent(activity, PantryListActivity::class.java)
                        activity!!.startActivity(intent)
                    } else {
                        toggleEnabledComponents()
                        Toast.makeText(activity,"Invalid email/password entered.", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}