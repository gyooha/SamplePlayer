package io.seroo.sampleplayer

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.seroo.sampleplayer.databinding.FragmentHomeBinding
import io.seroo.sampleplayer.home.AudioDTO

class HomeFragment : Fragment() {
    private val projection: Array<String> = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media.DURATION
    )

    private lateinit var fragmentHomeBinding: FragmentHomeBinding

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
        Log.d("GYH", "audioList : $audioList")
    }

    private fun getAudioList(): List<AudioDTO> {
        val audioList: MutableList<AudioDTO> = mutableListOf()

        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        context?.let { actualContext ->
            val cursor = actualContext.contentResolver.query(
                uri,
                projection,
                MediaStore.Audio.Media.IS_MUSIC + " != 0",
                null,
                null
            )

            cursor?.let { actualCursor ->
                while (actualCursor.moveToNext()) {
                    AudioDTO(
                        actualCursor.getString(0),
                        actualCursor.getString(1),
                        actualCursor.getString(2),
                        actualCursor.getString(3)
                    ).also { audioList.add(it) }
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