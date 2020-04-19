package com.example.pantry_organizer.planner.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.MealplanData
import com.example.pantry_organizer.data.RecipeData
import com.example.pantry_organizer.global.adapter.ViewPagerAdapter
import com.example.pantry_organizer.global.viewModel.AppViewModel
import com.example.pantry_organizer.pantry.adapter.PantryFoodListAdapter
import kotlinx.android.synthetic.main.activity_add_food.*
import kotlinx.android.synthetic.main.activity_pantry_food_list.*
import kotlinx.android.synthetic.main.fragment_week_view.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeekViewFragment(): Fragment() {
    lateinit var viewModel: AppViewModel
    private var dayList: ArrayList<String> = ArrayList()
    private var dayListMeals: ArrayList<MealplanData> = ArrayList()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val stringDate = this.arguments?.getString("weekMonday", "")
        val parsedDate = stringDate!!.split("/")
        val month = parsedDate[0].toInt()
        val day = parsedDate[1].toInt()
        val year = parsedDate[2].toInt()
        val dateTime = LocalDate.of(year, month, day)

        dayList = getWeekFromMondayDate(dateTime)
        //dayListWithMeals = get from repository by checking each day in daylist to see if there are associated recipes


        //Used for testing only, safe to delete


        val testDate1 = "9/15/2020"
        val testDate2 = "12/1/2019"
        val testDate3 = "1/16/202"
        val testList1 = listOf("Potato", "Tomato", "Death", "Taxes", "Coronavirus")
        val testList2 = listOf("Potato", "Tomato")
        val testList3 = listOf("Potato")

        val test1 = MealplanData(testDate1, testList1)
        val test2 = MealplanData(testDate2, testList2)
        val test3 = MealplanData(testDate3, testList3)

        Log.d("Daylist", dayList.size.toString())

        for(i in 0..6){
            dayListMeals.add(MealplanData(dayList[i], null))
        }
        dayListMeals[2] = MealplanData(dayList[2], testList1)
        dayListMeals[4] = MealplanData(dayList[4], testList2)
        dayListMeals[6] = MealplanData(dayList[6], testList3)

        return inflater.inflate(R.layout.fragment_week_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)

        val recyclerView = week_view_recycler_view
        val adapter = MealplanListAdapter(dayListMeals)
        recyclerView.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(this.activity)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getWeekFromMondayDate(dateTime: LocalDate): ArrayList<String>{
        var weekDayList = ArrayList<String>()
        var currentDate = dateTime
        for(i in 0..6){
            weekDayList.add(currentDate.format(DateTimeFormatter.ofPattern("M/d/y")))
            currentDate = currentDate.plusDays(1) //iterate to next day in week
        }
        return weekDayList
    }
}

