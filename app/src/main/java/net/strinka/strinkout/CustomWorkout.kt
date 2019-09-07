package net.strinka.strinkout

import androidx.annotation.NonNull
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "custom_workout")
class CustomWorkout(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "custom_workout_id") val customWorkoutId: Int,
    @NonNull val name: String,
    @NonNull val ordinal: Int,
    @NonNull val removed: Boolean
)

@Dao
interface CustomWorkoutDao {
    @Query("SELECT * FROM custom_workout WHERE removed = 0 ORDER BY ordinal ASC")
    fun getAllCustomWorkouts(): LiveData<List<CustomWorkout>>

    @Insert
    suspend fun insert(customWorkout: CustomWorkout)

    @Query("UPDATE custom_workout SET ordinal = ordinal + :shift WHERE ordinal >= :minIndex AND ordinal <= :maxIndex")
    suspend fun reOrder(minIndex: Int, maxIndex: Int, shift: Int)

    @Update
    suspend fun update(customWorkout: CustomWorkout)

    @Transaction
    suspend fun move(customWorkout: CustomWorkout, toOrdinal: Int){
        val fromOrdinal = customWorkout.ordinal
        if (fromOrdinal == toOrdinal)
            return
        if (fromOrdinal < toOrdinal)
            reOrder(fromOrdinal+1, toOrdinal, -1)
        else if (fromOrdinal > toOrdinal)
            reOrder(toOrdinal, fromOrdinal-1, 1)

        val newCustomWorkout = CustomWorkout(customWorkout.customWorkoutId, customWorkout.name, toOrdinal, customWorkout.removed)
        update(newCustomWorkout)
    }

    @Query("UPDATE custom_workout SET removed = 1 WHERE custom_workout_id = :customWorkoutId")
    fun remove(customWorkoutId: Int)

    @Query("SELECT MAX(ordinal) from custom_workout")
    fun getMaxOrdinal(): Int
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
    suspend fun move(customWorkout: CustomWorkout, toOrdinal: Int){
        customWorkoutDao.move(customWorkout, toOrdinal)
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