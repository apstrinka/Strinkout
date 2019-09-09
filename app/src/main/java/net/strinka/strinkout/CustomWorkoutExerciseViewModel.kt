package net.strinka.strinkout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomWorkoutExerciseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: StrinkoutRepository

    init {
        val database = StrinkoutDatabase.getDatabase(application, viewModelScope)
        val strinkoutDao = database.strinkoutDao()
        repository = StrinkoutRepository(strinkoutDao)
    }

    fun createWorkout(workout: Workout) = viewModelScope.launch(Dispatchers.IO) {
        repository.createWorkout(workout)
    }

}