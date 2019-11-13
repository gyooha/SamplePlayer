package io.seroo.sampleplayer.home

data class AudioDTO(
    val id: Long,
    val artist: String,
    val title: String,
    val audioPath: String,
    val albumPath: String
)