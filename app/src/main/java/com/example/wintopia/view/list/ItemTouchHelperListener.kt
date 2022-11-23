package com.example.wintopia.view.list

import androidx.recyclerview.widget.RecyclerView

interface ItemTouchHelperListener {

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
    fun onItemSwipe(position: Int)
    fun onLeftClick(position: Int, viewHolder: RecyclerView.ViewHolder?)
    fun onRightClick(position: Int, viewHolder: RecyclerView.ViewHolder?)

}
