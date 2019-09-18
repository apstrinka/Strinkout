package net.strinka.strinkout

import androidx.annotation.NonNull
import androidx.room.*

@Entity(tableName = "workout_exercise",
        foreignKeys = [ForeignKey(entity = Workout::class, parentColumns = arrayOf("workout_id"), childColumns = arrayOf("workout_id")),
                       ForeignKey(entity = Exercise::class, parentColumns = arrayOf("exercise_id"), childColumns = arrayOf("exercise_id"))]
)
class WorkoutExercise(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "workout_exercise_id") val workoutExerciseId: Int,
    @NonNull @ColumnInfo(name = "workout_id") val workoutId: Int,
    @NonNull @ColumnInfo(name = "exercise_id") val exerciseId: Int,
    @NonNull val ordinal: Int
)