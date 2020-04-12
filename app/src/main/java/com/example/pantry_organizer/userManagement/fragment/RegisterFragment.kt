package com.example.pantry_organizer.userManagement.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.pantry_organizer.R
import com.example.pantry_organizer.home.activity.HomeActivity
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment: UserManagementFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Turn off the warning layout.
        registerWarning_layout.visibility = View.INVISIBLE

        // Navigate to login click listener.
        login_navigate.setOnClickListener {
            val viewPager: ViewPager = activity!!.findViewById(R.id.userManagement_viewPager)
            viewPager.currentItem = 0
        }

        // Register button click listener.
        register_button.setOnClickListener {
            toggleEnabledComponents()
            registerWarning_layout.visibility = View.INVISIBLE

            // Harvest user input.
            val email = registerEmail_editText.text.toString()
            val password = registerPassword_editText.text.toString()
            val confirmPassword = registerConfirmPassword_editText.text.toString()
            val emailRegex = "^\\w+@\\w+[.][a-zA-z]{2,5}$".toRegex()
            registerPassword_editText.text.clear()
            registerConfirmPassword_editText.text.clear()

            // Sanitize input.
            if (email == "") {
                toggleEnabledComponents()
                printRegisterWarning("Email cannot be blank.")
                return@setOnClickListener
            } else if (!emailRegex.matches(email)) {
                toggleEnabledComponents()
                printRegisterWarning("Invalid email entered.")
                return@setOnClickListener
            } else if (password.length < 6) {
                toggleEnabledComponents()
                printRegisterWarning("Password must be at least 6 characters.")
                return@setOnClickListener
            } else if (password != confirmPassword) {
                toggleEnabledComponents()
                printRegisterWarning("Passwords do not match.")
                return@setOnClickListener
            }

            // Attempt to register with given credentials.
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Start the pantry list activity for this user.
                        val intent = Intent(activity, HomeActivity::class.java)
                        activity!!.startActivity(intent)
                    } else {
                        toggleEnabledComponents()
                        printRegisterWarning("This email has already been registered.")
                    }
                }
        }
    }
}