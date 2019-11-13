package io.seroo.sampleplayer.home

import com.bumptech.glide.Glide
import io.seroo.sampleplayer.common.BaseViewHolder1
import io.seroo.sampleplayer.databinding.SampleAudioViewHolderBinding

class SampleAudioViewHolder(
    private val binding: SampleAudioViewHolderBinding
) : BaseViewHolder1<AudioDTO>(binding.root) {
    override fun bindView(item: AudioDTO, position: Int) {
        Glide.with(itemView.context)
               .load(item.albumPath)
               .into(binding.albumThumbnail)

        binding.title.text = item.title
    }
}