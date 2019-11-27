package io.seroo.sampleplayer.db.common

import androidx.room.Dao

@Dao
interface BaseDAO {
    suspend fun<T> addAll(vararg item: T)
}