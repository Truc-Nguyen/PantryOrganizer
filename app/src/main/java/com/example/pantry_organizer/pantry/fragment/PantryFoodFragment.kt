package com.example.pantry_organizer.pantry.fragment
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.pantry_organizer.R
//import com.example.pantry_organizer.data.PantryData
//import com.example.pantry_organizer.global.fragment.AbstractPantryAppFragment
//import com.example.pantry_organizer.pantry.adapter.FoodDataAdapter
//import com.example.pantry_organizer.pantry.adapter.PantryDataAdapter
//import kotlinx.android.synthetic.main.fragment_food_list.*
//
//class PantryFoodFragment: AbstractPantryAppFragment() {
//    private var foodRecycler: RecyclerView? = null
//    private var mAdapter: PantryDataAdapter? = null
//
//    // firebase instances already set in AbstractPantryAppFragment.
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//
//        return inflater.inflate(R.layout.fragment_food_list, container, false)
//
//    }
//
//    override fun onStart() {
//        super.onStart()
//
//
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
//        add_food_button.setOnClickListener{
//            //functionality to add new food item to database
//
//            //functionality to add new food item to corresponding pantry_item.foodList (ArrayList<FoodData>())
//        }
//
//        back_button.setOnClickListener{
//            val fragment =
//                PantryFragment()
//            val transaction = activity!!.supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.home_frameLayout, fragment)
//            transaction.commit()
//        }
//
//    }
//
//    override fun onFoodItemClicked(pantry: PantryData) {
//
//        //navigate to food detail fragment
//    }
//}