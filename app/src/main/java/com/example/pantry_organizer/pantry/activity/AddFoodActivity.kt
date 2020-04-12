package com.example.pantry_organizer.pantry.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.pantry_organizer.R


import androidx.appcompat.app.AppCompatActivity
import com.example.pantry_organizer.global.adapter.ViewPagerAdapter
import com.example.pantry_organizer.pantry.fragment.AddCustomFoodFragment
import com.example.pantry_organizer.pantry.fragment.AddOnlineFoodFragment

import kotlinx.android.synthetic.main.activity_add_food.*

class AddFoodActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(AddOnlineFoodFragment(), "Search Online")
        viewPagerAdapter.addFragment(AddCustomFoodFragment(), "Custom Food")
        addFood_viewPager?.adapter = viewPagerAdapter
        addFood_tabLayout.setupWithViewPager(addFood_viewPager)
    }
}