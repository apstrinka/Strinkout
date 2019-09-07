package net.strinka.strinkout

import androidx.annotation.NonNull
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "custom_workout_exercise",
        foreignKeys = arrayOf(ForeignKey(entity = CustomWorkout::class, parentColumns = arrayOf("customWorkoutId"), childColumns = arrayOf("customWorkoutId"))))
class CustomWorkoutExercise(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "custom_workout_exercise_id") val customWorkoutExerciseId: Int,
    @NonNull @ColumnInfo(name = "custom_workout_id") val customWorkoutId: Int,
    @NonNull val exerciseId: Int,
    @NonNull val ordinal: Int
)

@Dao
interface CustomWorkoutExerciseDao {
    @Query("SELECT * FROM custom_workout_exercise WHERE custom_workout_id = :customWorkoutId")
    fun getExercisesFromWorkout(customWorkoutId: Int) : LiveData<List<CustomWorkoutExercise>>

    @Insert
    suspend fun insert(customWorkoutExercise: CustomWorkoutExercise)

    @Query("SELECT MAX(ordinal) from custom_workout_exercise where custom_workout_id = :customWorkoutId")
    fun getMaxOrdinal(customWorkoutId: Int): Int
}

class CustomWorkoutExerciseRepository(private val customWorkoutExerciseDao: CustomWorkoutExerciseDao, private val customWorkoutId: Int) {
    val allCustomWorkoutExercises = customWorkoutExerciseDao.getExercisesFromWorkout(customWorkoutId)

    fun getExercise(customWorkoutExercise: CustomWorkoutExercise): Exercise? {
        return allExercises[customWorkoutExercise.exerciseId]
    }

    fun getExercises(customWorkoutExercises: List<CustomWorkoutExercise>): List<Exercise> {
        return customWorkoutExercises.map { getExercise(it) }.filterNotNull()
    }

    @WorkerThread
    suspend fun insert(exerciseId: Int){
        val maxOrdinal = customWorkoutExerciseDao.getMaxOrdinal(customWorkoutId)
        val customWorkoutExercise = CustomWorkoutExercise(0, customWorkoutId, exerciseId, maxOrdinal+1)
        insert(customWorkoutExercise)
    }

    @WorkerThread
    suspend fun insert(customWorkoutExercise: CustomWorkoutExercise){
        customWorkoutExerciseDao.insert(customWorkoutExercise)
    }
}