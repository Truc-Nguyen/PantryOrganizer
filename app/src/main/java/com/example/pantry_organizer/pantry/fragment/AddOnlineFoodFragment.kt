package com.example.pantry_organizer.pantry.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.*
import com.example.pantry_organizer.global.viewModel.ViewModel
import com.example.pantry_organizer.home.activity.HomeActivity
import com.example.pantry_organizer.pantry.activity.AddFoodActivity
import com.example.pantry_organizer.pantry.adapter.FoodPreviewAdapter
import kotlinx.android.synthetic.main.activity_add_food.*
import kotlinx.android.synthetic.main.fragment_add_custom_food.*
import kotlinx.android.synthetic.main.fragment_add_online_food.*
import kotlinx.android.synthetic.main.fragment_food_list.*
import kotlinx.android.synthetic.main.fragment_login.*

class AddOnlineFoodFragment: Fragment() {
    lateinit var viewModel: ViewModel
    private var query: String? = null
    private var foodPreviewList: ArrayList<ApiFoodPreview> = ArrayList<ApiFoodPreview>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_online_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)

        val recyclerView = food_preview_recycler_view
        var foodPreviewAdapter = FoodPreviewAdapter(foodPreviewList, viewModel)
        recyclerView.adapter = foodPreviewAdapter
        recyclerView!!.layoutManager = LinearLayoutManager(this.context)

        online_search_button.setOnClickListener {
            Log.d("Online Food", "Button Clicked")

            query = online_search_box.text.toString()

            if(query != ""){
                viewModel.getFoodPreviews(query.toString())
            }else{
                val myToast = Toast.makeText(this.context,"Enter a query", Toast.LENGTH_SHORT)
                myToast.show()
            }
        }

        add_online_food_item.setOnClickListener {
            val myToast = Toast.makeText(this.context,"Add Online Food To Pantry", Toast.LENGTH_SHORT)
            myToast.show()
        }

        online_return_to_pantry.setOnClickListener {
            val intent = Intent(activity, HomeActivity::class.java)
            activity!!.startActivity(intent)
        }

        viewModel!!.foodPreviewList.observe(this, Observer{
            val queryResults = viewModel.foodPreviewList.value!!.common
            if(queryResults.isNotEmpty()) {
                foodPreviewList.clear()
                foodPreviewList.addAll(viewModel.foodPreviewList.value!!.common)
                foodPreviewAdapter.notifyDataSetChanged()
            }else{
                val myToast = Toast.makeText(this.context,"No Results Found", Toast.LENGTH_SHORT)
                myToast.show()
            }

        })
    }
}