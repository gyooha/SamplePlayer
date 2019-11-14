package io.seroo.sampleplayer.home

import android.content.Intent

sealed class HomeActions {
    data class MoveDetail(val audioDTO: AudioDTO) : HomeActions()
}