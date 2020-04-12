package com.example.pantry_organizer.userManagement.fragment

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.pantry_organizer.R
import com.google.firebase.auth.FirebaseAuth

abstract class UserManagementFragment: Fragment() {
    // Set firebase instances.
    val auth = FirebaseAuth.getInstance()

    // Toggle components enable flag.
    private var enableComponents: Boolean = true

    // Toggle user management components enabled property during firebase authentication.
    fun toggleEnabledComponents() {
        // Get view elements.
        val loginButton: Button = activity!!.findViewById(R.id.login_button)
        val loginEmail: View = activity!!.findViewById(R.id.loginEmail_editText)
        val loginPassword: View = activity!!.findViewById(R.id.loginPassword_editText)
        val registerButton: Button = activity!!.findViewById(R.id.register_button)
        val registerEmail: View = activity!!.findViewById(R.id.registerEmail_editText)
        val registerPassword: View = activity!!.findViewById(R.id.registerPassword_editText)
        val registerConfirmPassword: View = activity!!.findViewById(R.id.registerConfirmPassword_editText)

        // Toggle components.
        enableComponents = !enableComponents
        loginButton.isEnabled = enableComponents
        loginEmail.isEnabled = enableComponents
        loginPassword.isEnabled = enableComponents
        registerButton.isEnabled = enableComponents
        registerEmail.isEnabled = enableComponents
        registerPassword.isEnabled = enableComponents
        registerConfirmPassword.isEnabled = enableComponents

        // Toggle component text.
        if (enableComponents) {
            loginButton.text = resources.getString(R.string.login_fragment_title)
            registerButton.text = resources.getString(R.string.register_fragment_title)
        } else {
            loginButton.text = resources.getString(R.string.login_fragment_status)
            registerButton.text = resources.getString(R.string.register_fragment_status)
        }
    }

    // Prints a login warning message.
    fun printLoginWarning(message: String) {
        val layoutView: View = activity!!.findViewById(R.id.loginWarning_layout)
        val textView: TextView = activity!!.findViewById(R.id.loginWarning_textView)
        printWarning(layoutView, textView, message)
    }

    // Prints a register warning message.
    fun printRegisterWarning(message: String) {
        val layoutView: View = activity!!.findViewById(R.id.registerWarning_layout)
        val textView: TextView = activity!!.findViewById(R.id.registerWarning_textView)
        printWarning(layoutView, textView, message)
    }

    // Prints a warning message.
    private fun printWarning(layoutView: View, textView: TextView, message: String) {
        layoutView.visibility = View.VISIBLE
        textView.text = message
    }
}