package io.seroo.sampleplayer.detail

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navGraphViewModels
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import io.seroo.sampleplayer.MainViewModel
import io.seroo.sampleplayer.R
import io.seroo.sampleplayer.databinding.FragmentDetailBinding
import io.seroo.sampleplayer.db.Audio
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.sample_audio_controller.view.*

class PlayerDetailFragment : Fragment() {
    companion object {
        private const val USER_AGENT = "music_player"
        private const val SHARED_PREFERENCE_NAME = "musci_player_store"
        private const val KEY_PLAY_WHEN_READY = "key_play_when_ready"
        private const val KEY_CURRENT_WINDOW = "key_current_window"
        private const val KEY_PLAY_BACK_POSITION = "key_play_back_position"
    }

    private lateinit var fragmentDetailBinding: FragmentDetailBinding
    private lateinit var sharedPreference: SharedPreferences
    private val mainViewModel: MainViewModel by navGraphViewModels(R.id.nav_graph) {
        ViewModelProvider.NewInstanceFactory()
    }
    private var player: SimpleExoPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentDetailBinding.inflate(inflater, container, false).also {
        fragmentDetailBinding = it
    }.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragmentDetailBinding.run {
            lifecycleOwner = this@PlayerDetailFragment
        }

        context?.let { actualContext ->
            sharedPreference = actualContext
                .getSharedPreferences(
                    SHARED_PREFERENCE_NAME,
                    Context.MODE_PRIVATE
                )
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
            removeListener(playStateListener)
            release()
        }.also { player = null }
    }

    private fun initializePlayer() {
        context?.let { actualContext ->
            arguments?.getString(Audio.KEY_AUDIO_ID)?.let { data ->
                player = ExoPlayerFactory.newSimpleInstance(actualContext)
                player?.addListener(playStateListener)
                player?.addAnalyticsListener()
                music_player?.player = player

                mainViewModel.run {
                    currentMusic.observe(
                        this@PlayerDetailFragment,
                        Observer {
                            val currentMusic = it

                            fragmentDetailBinding.songTitle.text = currentMusic.title
                            fragmentDetailBinding.songArtist.text = currentMusic.artist

                            val mediaSource = buildMediaSource(
                                Uri.parse(currentMusic.audioPath)
                            )

                            sharedPreference.run {
                                player?.run {
                                    playWhenReady = getBoolean(KEY_PLAY_WHEN_READY, true)
                                    seekTo(getInt(KEY_CURRENT_WINDOW, 0), getLong(KEY_PLAY_BACK_POSITION, 0L))
                                    prepare(mediaSource)
                                }
                            }
                        }
                    )
                    findMusicById(data)
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

    private fun buildMediaSource(currentMusic: Uri): MediaSource {
        val mediaSourceFactory = ProgressiveMediaSource.Factory(
            DefaultDataSourceFactory(context, USER_AGENT)
        )

        return mediaSourceFactory.createMediaSource(currentMusic)
    }

    private fun startMusic() {
        player?.let { exoPlayer ->
            findRandomMusic()
        }
    }

    private val playStateListener = object: Player.EventListener {

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            fragmentDetailBinding
                .controllerGroup
                .btn_play_or_pause
                ?.isActivated = isPlaying
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_ENDED -> {

                }
            }
        }
    }

    private val analyticsListener = object: AnalyticsListener {

    }
}