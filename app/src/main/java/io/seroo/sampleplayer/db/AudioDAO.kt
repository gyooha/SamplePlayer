package io.seroo.sampleplayer.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import io.seroo.sampleplayer.db.common.BaseDAO

@Dao
interface AudioDAO : BaseDAO {
    suspend fun getAudioList(): LiveData<List<Audio>>
}