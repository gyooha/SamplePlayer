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
    private val _currentMusic: MutableLiveData<Audio> = MutableLiveData()
    val currentMusic: LiveData<Audio> get() = _currentMusic

    fun setMusicList(musicList: List<Audio>) {
        musicListWithoutLiveData = musicList
        _musicList.value = musicList
    }

    fun findTwoMusicsByIdOrRandom(id: String = "") {
        val result = musicListWithoutLiveData
            .firstOrNull { it.id.toString() == id }

        _currentMusic.value = if (result == null) {
            val randomInt = Random.nextInt(musicListWithoutLiveData.size)
            musicListWithoutLiveData[randomInt]
        } else {
            result
        }
    }
}