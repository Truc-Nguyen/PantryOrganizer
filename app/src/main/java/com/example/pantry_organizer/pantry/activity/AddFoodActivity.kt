package com.example.pantry_organizer.pantry.activity


import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.example.pantry_organizer.R
import com.example.pantry_organizer.global.adapter.ViewPagerAdapter
import com.example.pantry_organizer.pantry.fragment.AddCustomFoodFragment
import com.example.pantry_organizer.pantry.fragment.AddOnlineFoodFragment
import kotlinx.android.synthetic.main.activity_add_food.*


class AddFoodActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        // Support bar attributes.
        supportActionBar?.title = "New Food"
        supportActionBar?.subtitle = "Back"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    //    get current pantry name
        val currentPantry=intent.getStringExtra("AddFoodToPantry")
        var bundle = Bundle()
        bundle.putString("EnterPantry", currentPantry)
        val customFoodFragment = AddCustomFoodFragment()
        customFoodFragment.arguments = bundle

        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(AddOnlineFoodFragment(), "Search Online")
        viewPagerAdapter.addFragment(customFoodFragment, "Custom Food")

        addFood_viewPager?.adapter = viewPagerAdapter
        addFood_tabLayout.setupWithViewPager(addFood_viewPager)


//
//        //default fragment is add online food
//        val transaction = this!!.supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.frag_container,fragment)
//        transaction.commit()
    }

    // Inflate custom food activity app bar menu.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Return to previous activity.
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}