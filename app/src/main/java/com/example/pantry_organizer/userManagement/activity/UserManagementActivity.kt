package com.example.pantry_organizer.userManagement.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pantry_organizer.R
import com.example.pantry_organizer.global.adapter.ViewPagerAdapter
import com.example.pantry_organizer.userManagement.fragment.LoginFragment
import com.example.pantry_organizer.userManagement.fragment.RegisterFragment
import kotlinx.android.synthetic.main.activity_user_management.*

class UserManagementActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_management)

        // Hide the action bar.
        supportActionBar?.hide()

        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(LoginFragment(), "Log In")
        viewPagerAdapter.addFragment(RegisterFragment(), "Register")
        userManagement_viewPager?.adapter = viewPagerAdapter
        userManagement_tabLayout.setupWithViewPager(userManagement_viewPager)
    }

    // Block back button.
    override fun onBackPressed() {
    }
}