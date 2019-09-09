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