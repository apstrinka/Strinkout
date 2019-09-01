package net.strinka.strinkout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomWorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CustomWorkoutRepository
    val allCustomWorkouts: LiveData<List<CustomWorkout>>

    init {
        val dao = StrinkoutDatabase.getDatabase(application, viewModelScope).customWorkoutDao()
        repository = CustomWorkoutRepository(dao)
        allCustomWorkouts = repository.allCustomWorkouts
    }

    fun insert (name: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(name)
    }

    fun update(customWorkout: CustomWorkout) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(customWorkout)
    }

    fun remove(customWorkoutId: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.remove(customWorkoutId)
    }
}