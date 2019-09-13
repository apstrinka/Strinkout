package net.strinka.strinkout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class EditCustomWorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: StrinkoutRepository
    var workout: CustomWorkout? = null
    var exercises: List<Exercise> = emptyList()

    init {
        val database = StrinkoutDatabase.getDatabase(application, viewModelScope)
        val strinkoutDao = database.strinkoutDao()
        repository = StrinkoutRepository(strinkoutDao)
    }

    fun getWorkout(customWorkoutId: Int): Job = viewModelScope.launch(Dispatchers.IO) {
        workout = repository.getCustomWorkout(customWorkoutId)
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