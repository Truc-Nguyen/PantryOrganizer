package com.example.pantry_organizer.pantry.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.PantryData
import com.example.pantry_organizer.data.RecipeData
import com.example.pantry_organizer.global.viewModel.ViewModel
import com.example.pantry_organizer.home.activity.HomeActivity
import com.example.pantry_organizer.pantry.activity.AddFoodActivity
import com.example.pantry_organizer.pantry.adapter.PantryFoodListAdapter
import com.example.pantry_organizer.recipe.fragment.RecipeListAdapter
import kotlinx.android.synthetic.main.fragment_food_list.*
import kotlinx.android.synthetic.main.fragment_recipe_list.*

class PantryFoodListFragment: Fragment() {
    lateinit var viewModel: ViewModel
    var singlePantryFoods: List<String> = ArrayList()
    var pantryName: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)

        // Support bar attributes.
        val activity = this.activity as AppCompatActivity
        activity.supportActionBar?.subtitle = "Back"
        activity.supportActionBar?.setHomeButtonEnabled(true)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //retrieve arguments from previous fragment
        val bundle = this.arguments
        pantryName = bundle!!.getString("PantryName", "pantry")
//        viewModel.getSinglePantryFoods(pantryName!!)

        return inflater.inflate(R.layout.fragment_food_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("foodlistfrag", "created")

        //update page title
        val activity = this.activity as AppCompatActivity
        activity.supportActionBar?.title = pantryName

        Log.d("foodlistfrag", "pantry name: $pantryName")
        Log.d("foodlistfrag", "initial foods: $singlePantryFoods")
        //retrieve current pantry location

        viewModel.getSinglePantryFoods(pantryName!!)

        //Stuff commented out currently does not work, do not delete

        // Set up the recycler view to show food list.
        val recyclerView = food_recycler_view
        val adapter =
            PantryFoodListAdapter(
                singlePantryFoods
            )
        recyclerView.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(this.context)

        // Attach observer to food list
        if (singlePantryFoods != null){
            viewModel.singlePantryFoods.observe(this, Observer { liveData ->
                var mutable  = singlePantryFoods!! as MutableList<String>
                mutable.clear()
                mutable.addAll(liveData)
                singlePantryFoods = mutable
//            singlePantryFoods.clear()
//            singlePantryFoods.addAll(liveData)
                Log.d("foodlistfrag", "singlepantry foods: $singlePantryFoods")
                adapter.notifyDataSetChanged()
            })
        }


        add_food_button.setOnClickListener {
            val intent = Intent(activity, AddFoodActivity::class.java)
            intent.putExtra("AddFoodToPantry",pantryName)
            activity!!.startActivity(intent)
        }
    }
}