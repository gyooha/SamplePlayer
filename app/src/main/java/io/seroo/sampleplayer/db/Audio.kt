package io.seroo.sampleplayer.db

import androidx.room.Entity

@Entity(tableName = "audio")
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