package com.example.pantry_organizer.userManagement.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.pantry_organizer.R
import com.example.pantry_organizer.home.activity.HomeActivity
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment: UserManagementFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Turn off the warning layout.
        loginWarning_layout.visibility = View.INVISIBLE

        // Navigate to registration click listener.
        register_navigate.setOnClickListener {
            val viewPager: ViewPager = activity!!.findViewById(R.id.userManagement_viewPager)
            viewPager.currentItem = 1
        }

        // Login button click listener.
        login_button.setOnClickListener {
            toggleEnabledComponents()
            loginEmail_layout.background = resources.getDrawable(R.drawable.edit_text_border, null)
            loginPassword_layout.background = resources.getDrawable(R.drawable.edit_text_border, null)
            loginWarning_layout.visibility = View.INVISIBLE

            // Harvest user input.
            val email = loginEmail_editText.text.toString()
            val password = loginPassword_editText.text.toString()
            val emailRegex = "^\\w+@\\w+[.][a-zA-z]{2,5}$".toRegex()
            loginPassword_editText.text.clear()

            // Sanitize input.
            if (email == "") {
                toggleEnabledComponents()
                loginEmail_layout.background = resources.getDrawable(R.drawable.edit_text_border_red, null)
                printLoginWarning("Email cannot be blank.")
                return@setOnClickListener
            } else if (!emailRegex.matches(email)) {
                toggleEnabledComponents()

                loginEmail_layout.background = resources.getDrawable(R.drawable.edit_text_border_red, null)
                printLoginWarning("Invalid email entered.")
                return@setOnClickListener
            } else if (password.length < 6) {
                toggleEnabledComponents()
                loginEmail_layout.background = resources.getDrawable(R.drawable.edit_text_border_red, null)
                loginPassword_layout.background = resources.getDrawable(R.drawable.edit_text_border_red, null)
                printLoginWarning("Invalid email or password entered.")
                return@setOnClickListener
            }

            // Attempt user login with given credentials.
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Start the pantry list activity for this user.
                        val intent = Intent(activity, HomeActivity::class.java)
                        activity!!.startActivity(intent)
                    } else {
                        toggleEnabledComponents()
                        loginEmail_layout.background = resources.getDrawable(R.drawable.edit_text_border_red, null)
                        loginPassword_layout.background = resources.getDrawable(R.drawable.edit_text_border_red, null)
                        printLoginWarning("Invalid email or password entered.")
                    }
                }
        }
    }
}