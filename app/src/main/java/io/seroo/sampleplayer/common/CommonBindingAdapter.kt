package io.seroo.sampleplayer.common

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("submitList")
fun <T> submitOnRecyclerView(view: RecyclerView, itemList: List<T>) {
    (view.adapter as? BaseAdapter<T>)?.submit(itemList)
}