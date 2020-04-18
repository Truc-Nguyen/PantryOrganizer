package com.example.pantry_organizer.pantry.fragment

import android.app.AlertDialog
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.pantry_organizer.R
import com.example.pantry_organizer.data.PantryData
import com.example.pantry_organizer.global.adapter.SwipeController
import com.example.pantry_organizer.global.adapter.SwipeControllerActions
import com.example.pantry_organizer.global.viewModel.ViewModel
import com.example.pantry_organizer.pantry.adapter.PantryListAdapter
import kotlinx.android.synthetic.main.dialog_confirm_delete.*
import kotlinx.android.synthetic.main.fragment_pantry_list.*

class PantryListFragment: Fragment() {
    lateinit var viewModel: ViewModel
    private var pantryList= ArrayList<PantryData>()

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

        // Set up delete item swipe controller and button listeners.
        val swipeController = SwipeController(activity!!, 175f, object: SwipeControllerActions() {
            override fun setOnDeleteClicked(position: Int) {
                // Build an alert dialog for deleting this item.
                val deletePantryConfirmDialog = LayoutInflater.from(activity!!).inflate(R.layout.dialog_confirm_delete, null)
                val dialogBuilder = AlertDialog.Builder(activity!!)
                    .setView(deletePantryConfirmDialog)
                val dialog = dialogBuilder.show()

                // User confirms deletion.
                dialog.deleteItemConfirm_button.setOnClickListener{
                    dialog.dismiss()
                    // Delete the selected pantry.
                    viewModel.deletePantry(pantryList[position].name)
                }

                // User selects cancel.
                dialog.deleteItemCancel_button.setOnClickListener {
                    dialog.dismiss()
                }
            }
        })

        // Attach the swipe controller to the recycler view.
        ItemTouchHelper(swipeController).attachToRecyclerView(recyclerView)
        recyclerView.addItemDecoration(object : ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController.onDraw(c)
            }
        })

        // Attach observer to pantry data.
        viewModel.pantryList.observe(this, Observer { liveData ->
            pantryList.clear()
            pantryList.addAll(liveData)
            adapter.notifyDataSetChanged()
        })
    }
}