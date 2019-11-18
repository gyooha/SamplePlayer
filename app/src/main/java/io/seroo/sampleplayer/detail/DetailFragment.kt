package io.seroo.sampleplayer.detail

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import io.seroo.sampleplayer.databinding.FragmentDetailBinding
import io.seroo.sampleplayer.home.Audio
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {
    companion object {
        private const val USER_AGENT = "music_player"
        private const val SHARED_PREFERENCE_NAME = "musci_player_store"
        private const val KEY_PLAY_WHEN_READY = "key_play_when_ready"
        private const val KEY_CURRENT_WINDOW = "key_current_window"
        private const val KEY_PLAY_BACK_POSITION = "key_play_back_position"
    }

    private lateinit var fragmentDetailBinding: FragmentDetailBinding
    private lateinit var sharedPreference: SharedPreferences
    private var player: ExoPlayer? = null

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
            sharedPreference =
                actualContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

            arguments?.getParcelable<Audio>("Audio")?.let { data ->
                with (fragmentDetailBinding) {
                    songTitle.text = data.title
                    songArtist.text = data.artist
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        if (Util.SDK_INT < 24 || player == null) {
            initializePlayer()
        }
    }

    override fun onStop() {
        super.onStop()

        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    override fun onPause() {
        super.onPause()

        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        player?.run {
            sharedPreference.edit()?.run {
                putBoolean(KEY_PLAY_WHEN_READY, playWhenReady)
                putInt(KEY_CURRENT_WINDOW, currentWindowIndex)
                putLong(KEY_PLAY_BACK_POSITION, currentPosition)
                apply()
            }

            release()
        }.also { player = null }
    }

    private fun initializePlayer() {
        context?.let { actualContext ->
            arguments?.getParcelable<Audio>("Audio")?.let { data ->
                player = ExoPlayerFactory.newSimpleInstance(actualContext)
                music_player?.player = player
                val mediaSource = buildMediaSource(Uri.parse(data.audioPath))

                sharedPreference.run {
                    player?.run {
                        playWhenReady = getBoolean(KEY_PLAY_WHEN_READY, true)
                        seekTo(getInt(KEY_CURRENT_WINDOW, 0), getLong(KEY_PLAY_BACK_POSITION, 0L))
                        prepare(mediaSource)
                    }
                }
            }
        }

    }

    private fun hideSystemUI() {
        music_player?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    private fun buildMediaSource(uri: Uri): ProgressiveMediaSource {
        return ProgressiveMediaSource.Factory(DefaultDataSourceFactory(context, USER_AGENT))
            .createMediaSource(uri)
    }
}