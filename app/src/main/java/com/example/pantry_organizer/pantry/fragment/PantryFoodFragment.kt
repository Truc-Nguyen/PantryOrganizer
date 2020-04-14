package com.example.pantry_organizer.pantry.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.PantryData
import com.example.pantry_organizer.home.activity.HomeActivity
import com.example.pantry_organizer.pantry.activity.AddFoodActivity
//import com.example.pantry_organizer.pantry.adapter.FoodDataAdapter
import kotlinx.android.synthetic.main.fragment_food_list.*

class PantryFoodFragment: Fragment() {
    private var foodRecycler: RecyclerView? = null
//    private var mAdapter: FoodDataAdapter? = null

    // firebase instances already set in AbstractPantryAppFragment.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_food_list, container, false)

    }

    override fun onStart() {
        super.onStart()
        val pantryName = arguments?.getString("pantry_name")
        pantry_name.text = pantryName



//        foodRecycler = view!!.findViewById(R.id.food_recycler_view)
//
//        // Get users sorted by chips
//        val mQuery = db.collection("food_names")
//        mQuery.whereArrayContainsAny("regions", listOf("west_coast", "east_coast"))
//
//        // RecyclerView  -> fix this!
//        mAdapter = object : FoodDataAdapter(mQuery) {
//        }
//
//        foodRecycler!!.setLayoutManager(LinearLayoutManager(this.context))
//        foodRecycler!!.setAdapter(mAdapter)
        add_food_button.setOnClickListener{
            //functionality to add new food item to database

            //functionality to add new food item to corresponding pantry_item.foodList (ArrayList<FoodData>())

            //start activity with intent for result
            val intent = Intent(activity, AddFoodActivity::class.java)
            activity!!.startActivity(intent)
        }

        back_button.setOnClickListener{
            val fragment = PantryListFragment()
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.home_frameLayout, fragment)
            transaction.commit()
        }

    }

//    override fun onFoodItemClicked(pantry: PantryData) {
//
//        //navigate to food detail fragment
//    }
}