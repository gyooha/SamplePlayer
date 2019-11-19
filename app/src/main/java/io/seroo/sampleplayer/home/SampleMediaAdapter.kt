package io.seroo.sampleplayer.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import io.seroo.sampleplayer.common.BaseAdapter
import io.seroo.sampleplayer.common.BaseViewHolder
import io.seroo.sampleplayer.databinding.SampleAudioViewHolderBinding

class SampleMediaAdapter(
    private val homeActions: (HomeActions) -> Unit
) : BaseAdapter<Audio>() {
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder) {
            is SampleAudioViewHolder -> holder.bindView(getItem(position), position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return SampleAudioViewHolder(
            SampleAudioViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            homeActions
        )
    }

    override fun submit(newList: List<Audio>) {
        DiffUtil.calculateDiff(
            HomeListDiffUtil(newList = newList, oldList = itemList)
        ).dispatchUpdatesTo(this)
        itemList.run {
            clear()
            addAll(newList)
        }
    }
}