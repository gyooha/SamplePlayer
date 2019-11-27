package io.seroo.sampleplayer.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Audio::class],
    version = 1
)
abstract class SampleAudioDatabase: RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "sample_audio_database"

        @Volatile
        private var INSTANCE: SampleAudioDatabase? = null

        fun provideRoom(context: Context): SampleAudioDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    SampleAudioDatabase::class.java,
                    DATABASE_NAME
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}