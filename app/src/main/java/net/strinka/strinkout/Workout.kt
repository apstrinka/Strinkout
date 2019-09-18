package net.strinka.strinkout

import androidx.annotation.NonNull
import androidx.room.*

@Entity(tableName = "workout")
class Workout(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "workout_id") var workoutId: Int = 0,
    @NonNull var name: String = "",
    @NonNull var custom: Boolean = false,
    @NonNull var ordinal: Int = 0,
    @NonNull var removed: Boolean = false,
    @Ignore var exercises: List<Exercise> = emptyList()
)

fun workoutWithOrdinal(workout: Workout, newOrdinal: Int): Workout {
    return Workout(workout.workoutId, workout.name, workout.custom, newOrdinal, workout.removed, workout.exercises)
}