package io.seroo.sampleplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.seroo.sampleplayer.home.Audio
import kotlin.random.Random

class MainViewModel : ViewModel() {
    private var musicListWithoutLiveData: List<Audio> = listOf()
    private val _musicList = MutableLiveData<List<Audio>>(listOf())
    val musicList: LiveData<List<Audio>> get() = _musicList
    private val _pairMusic: MutableLiveData<Pair<Audio, Audio>> = MutableLiveData()
    val pairMusic: LiveData<Pair<Audio, Audio>> get() = _pairMusic

    fun setMusicList(musicList: List<Audio>) {
        musicListWithoutLiveData = musicList
        _musicList.value = musicList
    }

    fun findTwoMusicsById(id: String) {
        musicListWithoutLiveData
            .firstOrNull { it.id.toString() == id }
            ?.run {
                val randomInt = Random.nextInt(musicListWithoutLiveData.size)
                _pairMusic.value = this to musicListWithoutLiveData[randomInt]
            }
    }
}