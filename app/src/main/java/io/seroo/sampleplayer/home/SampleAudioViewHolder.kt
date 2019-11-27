package io.seroo.sampleplayer.home

import com.bumptech.glide.Glide
import io.seroo.sampleplayer.common.BaseViewHolder1
import io.seroo.sampleplayer.databinding.SampleAudioViewHolderBinding
import io.seroo.sampleplayer.db.Audio

class SampleAudioViewHolder(
    private val binding: SampleAudioViewHolderBinding,
    private inline val homeActions: (HomeActions) -> Unit
) : BaseViewHolder1<Audio>(binding.root) {
    override fun bindView(item: Audio, position: Int) {
        Glide.with(itemView.context)
               .load(item.albumPath)
               .into(binding.albumThumbnail)

        binding.title.text = item.title

        itemView.setOnClickListener {
            homeActions.invoke(HomeActions.MoveDetail(item.id.toString()))
        }
    }
}