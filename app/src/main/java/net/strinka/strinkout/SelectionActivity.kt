package net.strinka.strinkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders

const val MESSAGE_WORKOUT_CUSTOM = "net.strinka.strinkout.MESSAGE_WORKOUT_CUSTOM"
const val MESSAGE_WORKOUT = "net.strinka.strinkout.MESSAGE_WORKOUT"
const val MESSAGE_TIME = "net.strinka.strinkout.MESSAGE_TIME"
const val MESSAGE_COMPLETED = "net.strinka.strinkout.MESSAGE_COMPLETED"

class SelectionActivity : AppCompatActivity() {
    private lateinit var workoutViewModel: WorkoutViewModel
    var isCustomWorkout = false
    var workoutIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)
        isCustomWorkout = intent.getBooleanExtra(MESSAGE_WORKOUT_CUSTOM, false)
        workoutIndex = intent.getIntExtra(MESSAGE_WORKOUT, 0)

        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel::class.java)

        workoutViewModel.getWorkout(workoutIndex, isCustomWorkout).invokeOnCompletion {
            runOnUiThread {
                val workout = workoutViewModel.workout
                if (workout != null){
                    findViewById<TextView>(R.id.selected_workout).text = workout.name
                    val listView = findViewById<ListView>(R.id.activity_selection_list)
                    val array = workout.exercises.map { i -> i.name }.toTypedArray()
                    listView.adapter = ArrayAdapter<String>(this, R.layout.array_adapter_test, array)
                }
            }
        }
    }

    fun beginWorkout(view: View) {
        val time = findViewById<EditText>(R.id.select_time)
        val message = time.text.toString()
        val intent = Intent(this, WorkoutActivity::class.java).apply {
            putExtra(MESSAGE_TIME, message)
            putExtra(MESSAGE_WORKOUT_CUSTOM, isCustomWorkout)
            putExtra(MESSAGE_WORKOUT, workoutIndex)
        }
        startActivity(intent);
    }
}
