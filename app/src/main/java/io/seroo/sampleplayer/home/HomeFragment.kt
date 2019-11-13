package io.seroo.sampleplayer.home

import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.database.getStringOrNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import io.seroo.sampleplayer.common.PermissionUtils
import io.seroo.sampleplayer.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var sampleMediaAdapter: SampleMediaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentHomeBinding.inflate(inflater, container, false).also {
        fragmentHomeBinding = it
    }.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        PermissionUtils.requestPermission(this@HomeFragment) { initAction() }
    }

    private fun initAction() {
        val audioList = getAudioList()
        context?.let {
            sampleMediaAdapter = SampleMediaAdapter().apply {
                submit(audioList)
            }
            fragmentHomeBinding.playerList.apply {
                layoutManager = GridLayoutManager(it, 2)
                adapter = sampleMediaAdapter
            }
        }
        Log.d("GYH", "audioList : $audioList")
    }

    private fun getAudioList(): List<AudioDTO> {
        val audioList: MutableList<AudioDTO> = mutableListOf()

        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        context?.let { actualContext ->
            val cursor = actualContext.contentResolver.query(uri, null, null, null, null)

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
                            AudioDTO(
                                id,
                                cursor.getString(artistIndex),
                                cursor.getString(titleIndex),
                                ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id).toString(),
                                ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId).toString()
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
}