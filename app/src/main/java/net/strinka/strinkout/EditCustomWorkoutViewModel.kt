package net.strinka.strinkout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class EditCustomWorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: StrinkoutRepository
    var workout: Workout? = null
    var exercises: List<Exercise> = emptyList()
    val allExercises: LiveData<List<Exercise>>

    init {
        val database = StrinkoutDatabase.getDatabase(application, viewModelScope)
        val strinkoutDao = database.strinkoutDao()
        repository = StrinkoutRepository(strinkoutDao)
        allExercises = repository.allExercises
    }

    fun getWorkout(customWorkoutId: Int): Job = viewModelScope.launch(Dispatchers.IO) {
        workout = repository.getWorkout(customWorkoutId)
    }

    fun getExercises(customWorkoutId: Int): Job = viewModelScope.launch(Dispatchers.IO) {
        exercises = repository.getExercises(customWorkoutId)
    }

//    fun createWorkout(id: Int, name: String, custom: Boolean, exerciseIds: List<Int>) = viewModelScope.launch(Dispatchers.IO) {
//        repository.createWorkout(id, name, custom, exerciseIds)
//    }

    fun createWorkout(workout: Workout) = viewModelScope.launch(Dispatchers.IO) {
        repository.createWorkout(workout)
    }

    fun updateWorkout(customWorkoutId: Int, workout: Workout) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateWorkout(customWorkoutId, workout)
    }
}