package io.seroo.sampleplayer.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.seroo.sampleplayer.databinding.SampleAudioViewHolderBinding

class SampleMediaAdapter : RecyclerView.Adapter<SampleAudioViewHolder>() {
    private val sampleAudio: MutableList<AudioDTO> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleAudioViewHolder {
        return SampleAudioViewHolder(
            SampleAudioViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = sampleAudio.size

    override fun onBindViewHolder(holder: SampleAudioViewHolder, position: Int) {
        holder.bind()
    }
}