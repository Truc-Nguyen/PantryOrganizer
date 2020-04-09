package com.example.pantry_organizer.pantry.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.PantryData
import com.example.pantry_organizer.global.viewModel.ViewModel
import com.example.pantry_organizer.pantry.adapter.PantryListAdapter
import kotlinx.android.synthetic.main.fragment_pantry_list.*

class PantryListFragment: Fragment() {
    lateinit var viewModel: ViewModel
    private var pantryList: ArrayList<PantryData> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pantry_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)

        // Set up the recycler view to show pantry list.
        val recyclerView = pantry_recycler_view
        val adapter = PantryListAdapter(pantryList)
        recyclerView.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(this.context)

        // Attach observer to pantry list.
        viewModel.pantryList.observe(this, Observer { liveData ->
            pantryList.clear()
            pantryList.addAll(liveData)
            adapter.notifyDataSetChanged()
        })
    }
}