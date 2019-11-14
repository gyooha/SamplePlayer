package io.seroo.sampleplayer.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import io.seroo.sampleplayer.databinding.FragmentDetailBinding
import io.seroo.sampleplayer.home.AudioDTO

class DetailFragment: Fragment() {

    private lateinit var fragmentDetailBinding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentDetailBinding.inflate(inflater, container, false).also {
        fragmentDetailBinding = it
    }.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        context?.let { actualContext ->
            arguments?.getParcelable<AudioDTO>("audioDTO")?.let { data ->
                Glide.with(actualContext)
                    .load(data.albumPath)
                    .into(fragmentDetailBinding.albumImage)
            }
        }
    }
}