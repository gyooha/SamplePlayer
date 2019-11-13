package io.seroo.sampleplayer.home

import androidx.recyclerview.widget.DiffUtil

class HomeListDiffUtil(
    val newList: List<AudioDTO>,
    val oldList: List<AudioDTO>
): DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newList[newItemPosition] == oldList[oldItemPosition]

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newList[newItemPosition] == oldList[oldItemPosition]
}