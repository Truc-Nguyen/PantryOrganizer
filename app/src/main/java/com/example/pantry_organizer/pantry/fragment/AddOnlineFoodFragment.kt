package com.example.pantry_organizer.pantry.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.FoodData
import com.example.pantry_organizer.data.PantryData
import com.example.pantry_organizer.global.viewModel.ViewModel
import com.example.pantry_organizer.home.activity.HomeActivity
import com.example.pantry_organizer.pantry.activity.AddFoodActivity
import com.example.pantry_organizer.pantry.adapter.FoodDataAdapter
import kotlinx.android.synthetic.main.activity_add_food.*
import kotlinx.android.synthetic.main.fragment_add_custom_food.*
import kotlinx.android.synthetic.main.fragment_add_online_food.*
import kotlinx.android.synthetic.main.fragment_food_list.*
import kotlinx.android.synthetic.main.fragment_login.*

class AddOnlineFoodFragment: Fragment() {
    lateinit var viewModel: ViewModel
    private var foodPreviewList: ArrayList<FoodData> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_online_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)



        enter_online_food_item.setOnClickListener {
            val myToast = Toast.makeText(this.context,"Add Online Food To Pantry", Toast.LENGTH_SHORT)
            myToast.show()
        }

        online_return_to_pantry.setOnClickListener {
            val intent = Intent(activity, HomeActivity::class.java)
            activity!!.startActivity(intent)
        }


    }
}