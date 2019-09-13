package net.strinka.strinkout

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.CoroutineScope

@Database(entities = [CustomWorkout::class, CustomWorkoutExercise::class], version = 1)
public abstract class StrinkoutDatabase : RoomDatabase() {
    abstract fun strinkoutDao(): StrinkoutDao

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

@Dao
interface StrinkoutDao {
    @Query("SELECT * FROM custom_workout WHERE removed = 0 ORDER BY ordinal ASC")
    fun getAllCustomWorkouts(): LiveData<List<CustomWorkout>>

    @Query("SELECT * FROM custom_workout WHERE custom_workout_id = :customWorkoutId")
    fun getCustomWorkout(customWorkoutId: Int): CustomWorkout?

    @Insert
    suspend fun insert(customWorkout: CustomWorkout): Long

    @Query("UPDATE custom_workout SET ordinal = ordinal + :shift WHERE ordinal >= :minIndex AND ordinal <= :maxIndex")
    suspend fun reOrder(minIndex: Int, maxIndex: Int, shift: Int)

    @Update
    suspend fun update(customWorkout: CustomWorkout)

    @Query("UPDATE custom_workout SET name = :name WHERE custom_workout_id = :customWorkoutId")
    suspend fun updateCustomWorkoutName(customWorkoutId: Int, name: String)

    @Query("SELECT custom_workout_id FROM custom_workout WHERE rowid = :rowId")
    suspend fun getWorkoutId(rowId: Long): Int

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
    fun getMaxWorkoutOrdinal(): Int

    @Query("SELECT * FROM custom_workout_exercise WHERE custom_workout_id = :customWorkoutId")
    fun getExercisesFromWorkout(customWorkoutId: Int) : List<CustomWorkoutExercise>

    @Insert
    suspend fun insert(customWorkoutExercise: CustomWorkoutExercise)

    @Query("SELECT MAX(ordinal) from custom_workout_exercise WHERE custom_workout_id = :customWorkoutId")
    fun getMaxExerciseOrdinal(customWorkoutId: Int): Int

    @Query("DELETE FROM custom_workout_exercise WHERE custom_workout_id = :customWorkoutId")
    suspend fun removeExercisesFromWorkout(customWorkoutId: Int)

    @Transaction
    suspend fun createWorkout(workout: Workout){
        val maxOrdinal = getMaxWorkoutOrdinal()
        val customWorkout = CustomWorkout(0, workout.name, maxOrdinal+1, false)
        val rowid = insert(customWorkout)
        val customWorkoutId = getWorkoutId(rowid)

        var index = 0
        for (exercise in workout.exercises){
            val customWorkoutExercise = CustomWorkoutExercise(0, customWorkoutId, exercise.id, index)
            insert(customWorkoutExercise)
            index += 1
        }
    }

    @Transaction
    suspend fun updateWorkout(customWorkoutId: Int, workout: Workout){
        updateCustomWorkoutName(customWorkoutId, workout.name)
        removeExercisesFromWorkout(customWorkoutId)

        var index = 0
        for (exercise in workout.exercises){
            val customWorkoutExercise = CustomWorkoutExercise(0, customWorkoutId, exercise.id, index)
            insert(customWorkoutExercise)
            index += 1
        }
    }
}

class StrinkoutRepository(private val strinkoutDao: StrinkoutDao) {
    val allCustomWorkouts = strinkoutDao.getAllCustomWorkouts()

    @WorkerThread
    suspend fun move(customWorkout: CustomWorkout, toOrdinal: Int){
        strinkoutDao.move(customWorkout, toOrdinal)
    }

    @WorkerThread
    suspend fun remove(customWorkoutId: Int){
        strinkoutDao.remove(customWorkoutId)
    }

    @WorkerThread
    suspend fun getExercises(customWorkoutId: Int): List<Exercise> {
        val customWorkoutExercises = strinkoutDao.getExercisesFromWorkout(customWorkoutId)
        return getExercises(customWorkoutExercises)
    }

    fun getExercise(customWorkoutExercise: CustomWorkoutExercise): Exercise? {
        return allExercises[customWorkoutExercise.exerciseId]
    }

    fun getExercises(customWorkoutExercises: List<CustomWorkoutExercise>): List<Exercise> {
        return customWorkoutExercises.map { getExercise(it) }.filterNotNull()
    }

    @WorkerThread
    suspend fun getCustomWorkout(customWorkoutId: Int): CustomWorkout?{
        return strinkoutDao.getCustomWorkout(customWorkoutId)
    }

    @WorkerThread
    suspend fun createWorkout(workout: Workout){
        strinkoutDao.createWorkout(workout)
    }

    @WorkerThread
    suspend fun updateWorkout(customWorkoutId: Int, workout: Workout){
        strinkoutDao.updateWorkout(customWorkoutId, workout)
    }
}