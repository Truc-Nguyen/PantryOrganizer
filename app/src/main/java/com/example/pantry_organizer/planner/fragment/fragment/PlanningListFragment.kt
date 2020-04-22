package com.example.pantry_organizer.planner.fragment.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.pantry_organizer.R
import com.example.pantry_organizer.global.adapter.ViewPagerAdapter
import com.example.pantry_organizer.global.viewModel.AppViewModel

import kotlinx.android.synthetic.main.fragment_planning_week.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PlanningListFragment: Fragment() {
    lateinit var viewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_planning_week, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)

        val currentDate = LocalDateTime.now()
        val currentWeekDay = currentDate.dayOfWeek.toString()
        val previousMonday = currentDate.minusDays(daysToWeekStart(currentWeekDay))
        val nextMonday  = currentDate.plusDays(7).minusDays(daysToWeekStart(currentWeekDay))
        val previousMondayFormatted = previousMonday.format(DateTimeFormatter.ofPattern("M.d.y"))
        val nextMondayFormatted = nextMonday.format(DateTimeFormatter.ofPattern("M.d.y"))

        var currentWeekBundle = Bundle()
        currentWeekBundle.putString("weekMonday", previousMondayFormatted)

        var nextWeekBundle = Bundle()
        nextWeekBundle.putString("weekMonday", nextMondayFormatted)

        val currentWeek =
            WeekViewFragment()
        currentWeek.arguments = currentWeekBundle
        val nextWeek =
            WeekViewFragment()
        nextWeek.arguments = nextWeekBundle

        val viewPagerAdapter = ViewPagerAdapter(activity!!.supportFragmentManager)
        viewPagerAdapter.addFragment(currentWeek, "Current Week")
        viewPagerAdapter.addFragment(nextWeek, "Next Week")
        planning_viewPager?.adapter = viewPagerAdapter
        planning_tabLayout.setupWithViewPager(planning_viewPager)

    }

    // Swap out a fragment on the home activity.
    private fun swapFragment(title: String, fragment: Fragment, menu: Int?) {
        // Swap in the fragment.
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.planning_frag_container, fragment)
        transaction.commit()
    }

    private fun daysToWeekStart(weekday: String): Long {
        var daysFromWeekStart: Long = 0
        when (weekday) {
            "MONDAY"    -> daysFromWeekStart = 0
            "TUESDAY"   -> daysFromWeekStart = 1
            "WEDNESDAY" -> daysFromWeekStart = 2
            "THURSDAY"  -> daysFromWeekStart = 3
            "FRIDAY"    -> daysFromWeekStart = 4
            "SATURDAY"  -> daysFromWeekStart = 5
            "SUNDAY"    -> daysFromWeekStart = 6
        }
        return daysFromWeekStart
    }
}
