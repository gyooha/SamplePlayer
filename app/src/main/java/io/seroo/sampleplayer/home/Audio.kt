package io.seroo.sampleplayer.home

data class Audio(
    val id: Long,
    val artist: String,
    val title: String,
    val audioPath: String,
    val albumPath: String
) {
    companion object {
        const val KEY_AUDIO_ID = "key_audio_id"
    }
}