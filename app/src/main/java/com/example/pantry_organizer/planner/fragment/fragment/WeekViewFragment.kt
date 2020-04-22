package com.example.pantry_organizer.planner.fragment.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.MealplanData
import com.example.pantry_organizer.data.RecipeData
import com.example.pantry_organizer.global.viewModel.AppViewModel
import com.example.pantry_organizer.planner.fragment.adapter.MealplanListAdapter
import kotlinx.android.synthetic.main.fragment_week_view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WeekViewFragment(): Fragment() {
    lateinit var viewModel: AppViewModel
    private var dayList: ArrayList<String> = ArrayList()
    private var dayListMeals: ArrayList<MealplanData> = ArrayList()
    private var adapter: MealplanListAdapter? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val stringDate = this.arguments?.getString("weekMonday", "")
        val parsedDate = stringDate!!.split(".")
        val month = parsedDate[0].toInt()
        val day = parsedDate[1].toInt()
        val year = parsedDate[2].toInt()
        val dateTime = LocalDate.of(year, month, day)
        dayList = getWeekFromMondayDate(dateTime)

        //Used for testing only, safe to delete

        Log.d("Daylist", dayList.toString())
        Log.d("Daylist Meals", dayListMeals.toString())
//
        for(i in 0..6){
            dayListMeals.add(MealplanData(dayList[i], null))
        }


        //Used for testing only, safe to delete

        return inflater.inflate(R.layout.fragment_week_view, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)

        val recyclerView = week_view_recycler_view
        adapter = MealplanListAdapter(dayListMeals)
        recyclerView.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(this.activity)

        viewModel.getRecipesForWeek(dayList)
        for(x in 0..6) {
            viewModel.weekMeals.value!![x].observe(this, Observer {
                Log.d("Daylist Meals", "Change Observed")
                Log.d("Day: ", dayList[x])
                val recipeList = it
                Log.d("Recipes: ", recipeList.toString())
                val mealData = MealplanData(dayList[x], recipeList)
                dayListMeals[x] = mealData
                adapter!!.notifyDataSetChanged()
            })
        }
    }

    override fun onResume(){
        super.onResume()
        viewModel.getRecipesForWeek(dayList)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getWeekFromMondayDate(dateTime: LocalDate): ArrayList<String>{
        var weekDayList = ArrayList<String>()
        var currentDate = dateTime
        for(i in 0..6){
            weekDayList.add(currentDate.format(DateTimeFormatter.ofPattern("M.d.y")))
            currentDate = currentDate.plusDays(1) //iterate to next day in week
        }
        return weekDayList
    }
}

