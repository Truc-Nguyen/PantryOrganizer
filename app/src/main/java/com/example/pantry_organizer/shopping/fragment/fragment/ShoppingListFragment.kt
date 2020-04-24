package com.example.pantry_organizer.planner.fragment.fragment

import android.app.AlertDialog
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import com.example.pantry_organizer.global.viewModel.AppViewModel
import kotlinx.android.synthetic.main.fragment_shopping.*
import com.example.pantry_organizer.data.ShoppingData
import com.example.pantry_organizer.global.adapter.SwipeController
import com.example.pantry_organizer.global.adapter.SwipeControllerActions
import com.example.pantry_organizer.shopping.fragment.adapter.ShoppingListAdapter
import kotlinx.android.synthetic.main.dialog_confirm_remove_item.*

class ShoppingListFragment: Fragment() {
    lateinit var viewModel: AppViewModel
    private var shoppingList = ArrayList<ShoppingData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shopping, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)

        val recyclerView = shopping_recyclerView
        val adapter = ShoppingListAdapter(shoppingList)
        recyclerView.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(this.context)

        // Attach observer to shopping list data.
        viewModel.shoppingList.observe(activity!!, Observer { liveData ->
            shoppingList.clear()
            shoppingList.addAll(liveData)
            adapter.notifyDataSetChanged()
        })

        // Set up delete item swipe controller and button listeners.
        val swipeController = SwipeController(activity!!, 175f, object: SwipeControllerActions() {
            override fun setOnDeleteClicked(position: Int) {
                // Build an alert dialog for deleting this item.
                val deleteShoppingListItemConfirmDialog = LayoutInflater.from(activity!!).inflate(
                    R.layout.dialog_confirm_remove_item, null)
                val dialogBuilder = AlertDialog.Builder(activity!!)
                    .setView(deleteShoppingListItemConfirmDialog)
                val dialog = dialogBuilder.show()

                // Update the remove food message.
                val messageView: TextView = dialog.findViewById(R.id.removeItemMessage_textView)
                val message =
                    "Are you sure you want to remove ${shoppingList[position].name} from you list?"
                messageView.text = message

                // User confirms deletion.
                dialog.removeItemConfirm_button.setOnClickListener {
                    // Define views.
                    val qtyView: EditText = dialog.findViewById(R.id.removeItemQuantity_editText)
                    val confirmButton: Button = dialog.findViewById(R.id.removeItemConfirm_button)
                    val cancelButton: Button = dialog.findViewById(R.id.removeItemCancel_button)

                    // Reset quantity field color.
                    qtyView.background = resources.getDrawable(R.drawable.edit_text_border, null)

                    // Sanitize input.
                    val input = qtyView.text.toString()
                    if (input == "") {
                        Toast.makeText(activity!!,"Please enter a valid quantity.", Toast.LENGTH_LONG).show()
                        qtyView.background = resources.getDrawable(R.drawable.edit_text_border_red, null)
                        return@setOnClickListener
                    }

                    // Extract the quantity of food to remove.
                    val qtyToRemove = input.toLong()

                    // Disable the buttons once a request has been made.
                    confirmButton.isEnabled = false
                    cancelButton.isEnabled = false

                    // Delete the selected pantry.
                    viewModel.removeShoppingListItem(shoppingList[position], qtyToRemove)
                    dialog.dismiss()
                }
                // User selects cancel.
                dialog.removeItemCancel_button.setOnClickListener {
                    dialog.dismiss()
                }
            }
        })

//         Attach the swipe controller to the recycler view.
        ItemTouchHelper(swipeController).attachToRecyclerView(recyclerView)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController.onDraw(c)
            }
        })
    }
}
