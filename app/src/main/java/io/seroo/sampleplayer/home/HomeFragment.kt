package io.seroo.sampleplayer.home

import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import io.seroo.sampleplayer.MainViewModel
import io.seroo.sampleplayer.R
import io.seroo.sampleplayer.common.PermissionUtils
import io.seroo.sampleplayer.databinding.FragmentHomeBinding
import io.seroo.sampleplayer.db.Audio

class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var sampleMediaAdapter: SampleMediaAdapter

    private val mainViewModel: MainViewModel by navGraphViewModels(R.id.nav_graph) { ViewModelProvider.NewInstanceFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentHomeBinding.inflate(inflater, container, false).also {
        fragmentHomeBinding = it
    }.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragmentHomeBinding.run {
            lifecycleOwner = this@HomeFragment
            vm = mainViewModel
        }
        PermissionUtils.requestPermission(this@HomeFragment) { initAction() }
    }

    private fun initAction() {
        mainViewModel.setMusicList(getAudioList())
        context?.let {
            sampleMediaAdapter = SampleMediaAdapter(
                homeActions
            )

            fragmentHomeBinding.playerList.apply {
                layoutManager = GridLayoutManager(it, 2)
                adapter = sampleMediaAdapter
            }
        }
    }

    private fun getAudioList(): List<Audio> {
        val audioList: MutableList<Audio> = mutableListOf()

        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        context?.let { actualContext ->
            val cursor = actualContext.contentResolver.query(
                uri,
                null,
                MediaStore.Audio.Media.IS_MUSIC + "!= 0",
                null,
                null
            )

            when {
                cursor == null -> {
                    //TODO Query Fail
                }
                !cursor.moveToFirst() -> {
                    //TODO no media error
                }
                else -> {
                    val idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                    val titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                    val artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                    val albumIdIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)

                    do {
                        val id = cursor.getLong(idIndex)
                        val albumId = cursor.getLong(albumIdIndex)
                        audioList.add(
                            Audio(
                                id,
                                cursor.getString(artistIndex),
                                cursor.getString(titleIndex),
                                ContentUris.withAppendedId(
                                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                    id
                                ).toString(),
                                ContentUris.withAppendedId(
                                    Uri.parse("content://media/external/audio/albumart"),
                                    albumId
                                ).toString()
                            )
                        )
                    } while (cursor.moveToNext())
                }
            }

            cursor?.close()
        }

        return audioList
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            PermissionUtils.REQUEST_SETTINGS -> {
                activity?.let {
                    PermissionUtils.requestPermission(this@HomeFragment) { initAction() }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (PermissionUtils.REQUEST_PERMISSION == requestCode) {
            PermissionUtils.onRequestPermissionResult(
                grantResults,
                onGrantedAction = { initAction() },
                onDeniedPermission = {
                    activity?.let {
                        PermissionUtils.requestPermission(
                            this@HomeFragment
                        ) { initAction() }
                    }
                }
            )
        }
    }

    private val homeActions: (HomeActions) -> Unit = { action ->
        when (action) {
            is HomeActions.MoveDetail -> {
                findNavController().navigate(
                    R.id.action_homeFragment_to_detailFragment,
                    bundleOf(Audio.KEY_AUDIO_ID to action.audioId)
                )
            }
        }
    }
}