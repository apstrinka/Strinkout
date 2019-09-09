package net.strinka.strinkout

import androidx.annotation.NonNull
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "custom_workout_exercise",
        foreignKeys = arrayOf(ForeignKey(entity = CustomWorkout::class, parentColumns = arrayOf("custom_workout_id"), childColumns = arrayOf("custom_workout_id"))))
class CustomWorkoutExercise(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "custom_workout_exercise_id") val customWorkoutExerciseId: Int,
    @NonNull @ColumnInfo(name = "custom_workout_id") val customWorkoutId: Int,
    @NonNull val exerciseId: Int,
    @NonNull val ordinal: Int
)