package net.strinka.strinkout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomWorkoutsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: StrinkoutRepository
    val allCustomWorkouts: LiveData<List<Workout>>

    init {
        val dao = StrinkoutDatabase.getDatabase(application, viewModelScope).strinkoutDao()
        repository = StrinkoutRepository(dao)
        allCustomWorkouts = repository.allCustomWorkouts
    }

    fun moveItem(from: Int, to: Int) = viewModelScope.launch(Dispatchers.IO) {
        val workoutFrom = allCustomWorkouts.value!![from]
        val workoutTo = allCustomWorkouts.value!![to]
        val toOrdinal = workoutTo.ordinal
        repository.move(workoutFrom, toOrdinal)
    }

    fun remove(customWorkoutId: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.remove(customWorkoutId)
    }
}