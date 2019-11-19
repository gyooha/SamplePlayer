package io.seroo.sampleplayer.home

sealed class HomeActions {
    data class MoveDetail(val audioId: String) : HomeActions()
}