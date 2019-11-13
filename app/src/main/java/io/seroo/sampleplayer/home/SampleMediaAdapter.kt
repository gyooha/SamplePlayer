package io.seroo.sampleplayer.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.seroo.sampleplayer.common.BaseViewHolder
import io.seroo.sampleplayer.databinding.SampleAudioViewHolderBinding

class SampleMediaAdapter : RecyclerView.Adapter<BaseViewHolder>() {
    private val sampleAudio: MutableList<AudioDTO> = mutableListOf()

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder) {
            is SampleAudioViewHolder -> holder.bindView(sampleAudio[position], position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return SampleAudioViewHolder(
            SampleAudioViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = sampleAudio.size

    fun submit(newList: List<AudioDTO>) {
        DiffUtil.calculateDiff(
            HomeListDiffUtil(newList = newList, oldList = sampleAudio)
        ).dispatchUpdatesTo(this)
        sampleAudio.run {
            clear()
            addAll(newList)
        }
    }
}