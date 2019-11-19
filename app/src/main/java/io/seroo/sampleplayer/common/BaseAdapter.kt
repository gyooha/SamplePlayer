package io.seroo.sampleplayer.common

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T>(
    protected val itemList: MutableList<T> = mutableListOf()
) : RecyclerView.Adapter<BaseViewHolder>() {

    fun getItem(position: Int) = itemList[position]

    override fun getItemCount(): Int = itemList.size

    abstract fun submit(newList: List<T>)
}