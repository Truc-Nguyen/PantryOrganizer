package com.example.pantry_organizer.global.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.example.pantry_organizer.R
import kotlin.math.min

abstract class SwipeControllerActions {
    open fun setOnDeleteClicked(position: Int) {}
}

internal enum class ButtonsState {
    NONE,
    TRASH
}

class SwipeController(private val activity: Activity,  private val buttonWidth: Float,
                      private var buttonsActions: SwipeControllerActions?): ItemTouchHelper.Callback() {
    private var swipeBack = false
    private var buttonShowedState = ButtonsState.NONE
    private var buttonInstance: RectF? = null
    private var currentItemViewHolder: RecyclerView.ViewHolder? = null
    private val cornerRadius = 16f
    private val margin = 15

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(0, LEFT)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonsState.NONE
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                             dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        var dx = dX
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonsState.NONE) {
                if (buttonShowedState == ButtonsState.TRASH) {
                    dx = min(dx, -buttonWidth)
                }
                super.onChildDraw(c, recyclerView, viewHolder, dx, dY, actionState, isCurrentlyActive)
            } else {
                setTouchListener(c, recyclerView, viewHolder, dx, dY, actionState, isCurrentlyActive)
            }
        }

        if (buttonShowedState == ButtonsState.NONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dx, dY, actionState, isCurrentlyActive)
        }

        currentItemViewHolder = viewHolder
        onDraw(c)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                 dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        recyclerView.setOnTouchListener { _, event ->
            swipeBack = event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
            if (swipeBack) {
                if (dX < -buttonWidth) {
                    buttonShowedState =  ButtonsState.TRASH
                }

                if (buttonShowedState != ButtonsState.NONE) {
                    setTouchDownListener(c, recyclerView, viewHolder, dY, actionState, isCurrentlyActive)
                    setItemsClickable(recyclerView, false)
                }
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchDownListener(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                     dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        recyclerView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(c, recyclerView, viewHolder, dY, actionState, isCurrentlyActive)
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchUpListener(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                   dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        recyclerView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                super@SwipeController.onChildDraw(c, recyclerView, viewHolder,0f, dY, actionState, isCurrentlyActive)
                recyclerView.setOnTouchListener { _, _ ->
                    false
                }
                setItemsClickable(recyclerView, true)
                swipeBack = false
                if (buttonsActions != null && buttonInstance != null &&
                    buttonInstance!!.contains(event.x, event.y)) {
                    if (buttonShowedState == ButtonsState.TRASH) {
                        buttonsActions!!.setOnDeleteClicked(viewHolder.adapterPosition)
                    }
                }
                buttonShowedState = ButtonsState.NONE
                currentItemViewHolder = null
            }
            false
        }
    }

    private fun setItemsClickable(recyclerView: RecyclerView, isClickable: Boolean) {
        for (i in 0 until recyclerView.childCount) {
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }

    private fun drawButtons(c: Canvas, viewHolder: RecyclerView.ViewHolder) {
        val itemView: View = viewHolder.itemView

        // Paint the bottom layer red.
        val paintBounds = RectF(
            itemView.left.toFloat() + margin,
            itemView.top.toFloat() + margin,
            itemView.right.toFloat() - margin,
            itemView.bottom.toFloat() - margin
        )
        val p = Paint()
        p.color = ContextCompat.getColor(activity, R.color.red)
        c.drawRoundRect(paintBounds, cornerRadius, cornerRadius, p)

        // Draw the trash icon.
        val left = itemView.right - buttonWidth.toInt() - margin / 2
        val top = itemView.top + margin
        val right = itemView.right - margin
        val bottom = itemView.bottom - margin
        val x = right - ((right - left) / 2)
        val y = bottom - ((bottom - top) / 2)
        val iconBounds = Rect(
            x - 30,
            y - 40,
            x + 30,
            y + 40
        )
        val d = activity.resources.getDrawable(R.drawable.trash_icon, null)
        d.bounds = iconBounds
        d.draw(c)

        // Define clickable area bounds for firing button action.
        val clickBounds = RectF(
            itemView.right - buttonWidth + margin,
            itemView.top.toFloat() + margin,
            itemView.right.toFloat() - margin,
            itemView.bottom.toFloat() - margin
        )
        buttonInstance = null
        if (buttonShowedState == ButtonsState.TRASH) {
            buttonInstance = clickBounds
        }
    }

    fun onDraw(c: Canvas) {
        if (currentItemViewHolder != null) {
            drawButtons(c, currentItemViewHolder!!)
        }
    }
}