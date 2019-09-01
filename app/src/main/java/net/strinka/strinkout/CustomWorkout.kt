package net.strinka.strinkout

import android.content.Context
import androidx.annotation.NonNull
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.CoroutineScope

@Entity(tableName = "custom_workout")
class CustomWorkout(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "custom_workout_id") val customWorkoutId: Int,
    @NonNull val name: String,
    @NonNull val ordinal: Double,
    @NonNull val removed: Boolean
)

@Dao
interface CustomWorkoutDao {
    @Query("SELECT * from custom_workout WHERE removed = 0 ORDER BY ordinal ASC")
    fun getAllCustomWorkouts(): LiveData<List<CustomWorkout>>

    @Insert
    suspend fun insert(customWorkout: CustomWorkout)

    @Update
    suspend fun update(customWorkout: CustomWorkout)

    @Query("UPDATE custom_workout SET removed = 1 WHERE custom_workout_id = :customWorkoutId")
    fun remove(customWorkoutId: Int)

    @Query("SELECT MAX(ordinal) from custom_workout")
    fun getMaxOrdinal(): Double
}

class CustomWorkoutRepository(private val customWorkoutDao: CustomWorkoutDao) {
    val allCustomWorkouts = customWorkoutDao.getAllCustomWorkouts()

    @WorkerThread
    suspend fun insert(name: String){
        val maxOrdinal = customWorkoutDao.getMaxOrdinal()
        val customWorkout = CustomWorkout(0, name, maxOrdinal+1, false)
        insert(customWorkout)
    }

    @WorkerThread
    suspend fun insert(customWorkout: CustomWorkout){
        customWorkoutDao.insert(customWorkout)
    }

    @WorkerThread
    suspend fun update(customWorkout: CustomWorkout){
        customWorkoutDao.update(customWorkout)
    }

    @WorkerThread
    suspend fun remove(customWorkoutId: Int){
        customWorkoutDao.remove(customWorkoutId)
    }
}