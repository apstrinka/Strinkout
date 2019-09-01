package net.strinka.strinkout

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [CustomWorkout::class], version = 1)
public abstract class StrinkoutDatabase : RoomDatabase() {
    abstract fun customWorkoutDao(): CustomWorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: StrinkoutDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): StrinkoutDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, StrinkoutDatabase::class.java, "Strinkout").build()
                INSTANCE = instance
                instance
            }
        }
    }
}