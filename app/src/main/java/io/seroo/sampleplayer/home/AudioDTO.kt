package io.seroo.sampleplayer.home

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class AudioDTO(
    val id: Long,
    val artist: String,
    val title: String,
    val audioPath: String,
    val albumPath: String
) : Parcelable