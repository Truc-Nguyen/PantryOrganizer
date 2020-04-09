package com.example.pantry_organizer.userManagement.fragment

import android.view.View
import android.widget.Button
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
        val signUpButton: Button = activity!!.findViewById(R.id.signUp_button)
        val signUpEmail: View = activity!!.findViewById(R.id.signUpEmail_editText)
        val signUpPassword: View = activity!!.findViewById(R.id.signUpPassword_editText)
        val signUpConfirmPassword: View = activity!!.findViewById(R.id.signUpConfirmPassword_editText)

        // Toggle components.
        enableComponents = !enableComponents
        loginButton.isEnabled = enableComponents
        loginEmail.isEnabled = enableComponents
        loginPassword.isEnabled = enableComponents
        signUpButton.isEnabled = enableComponents
        signUpEmail.isEnabled = enableComponents
        signUpPassword.isEnabled = enableComponents
        signUpConfirmPassword.isEnabled = enableComponents

        // Toggle component text.
        if (enableComponents) {
            loginButton.text = resources.getString(R.string.login_fragment_title)
            signUpButton.text = resources.getString(R.string.sign_up_fragment_title)
        } else {
            loginButton.text = resources.getString(R.string.login_fragment_status)
            signUpButton.text = resources.getString(R.string.sign_up_fragment_status)
        }
    }
}