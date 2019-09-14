package net.strinka.strinkout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: StrinkoutRepository
    var workout: Workout? = null
    var exercises: List<Exercise> = emptyList()

    init {
        val database = StrinkoutDatabase.getDatabase(application, viewModelScope)
        val strinkoutDao = database.strinkoutDao()
        repository = StrinkoutRepository(strinkoutDao)
    }

    fun getWorkout(workoutId: Int, isCustomWorkout: Boolean): Job = viewModelScope.launch(Dispatchers.IO) {
        if (!isCustomWorkout){
            workout = defaultWorkouts[workoutId]
            return@launch
        }

        val customWorkout = repository.getCustomWorkout(workoutId)
        if (customWorkout == null) {
            workout = null
            return@launch
        }

        val exercises = repository.getExercises(workoutId)
        workout = Workout(customWorkout.name, exercises)
    }

    fun getExercises(customWorkoutId: Int): Job = viewModelScope.launch(Dispatchers.IO) {
        exercises = repository.getExercises(customWorkoutId)
    }

    fun createWorkout(workout: Workout) = viewModelScope.launch(Dispatchers.IO) {
        repository.createWorkout(workout)
    }

    fun updateWorkout(customWorkoutId: Int, workout: Workout) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateWorkout(customWorkoutId, workout)
    }
}