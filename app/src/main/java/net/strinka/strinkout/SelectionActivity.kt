package net.strinka.strinkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView

const val MESSAGE_WORKOUT = "net.strinka.strinkout.MESSAGE_WORKOUT"
const val MESSAGE_TIME = "net.strinka.strinkout.MESSAGE_TIME"

class SelectionActivity : AppCompatActivity() {
    var workoutIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)
        workoutIndex = intent.getIntExtra(MESSAGE_WORKOUT, 0)
        findViewById<TextView>(R.id.selected_workout).text = defaultWorkouts[workoutIndex].name
        val listView = findViewById<ListView>(R.id.activity_selection_list)
        val array = defaultWorkouts[workoutIndex].exercises.map { i -> i.name }.toTypedArray()
        listView.adapter = ArrayAdapter<String>(this, R.layout.array_adapter_test, array)
    }

    fun beginWorkout(view: View) {
        val time = findViewById<EditText>(R.id.select_time)
        val message = time.text.toString()
        val intent = Intent(this, WorkoutActivity::class.java).apply {
            putExtra(MESSAGE_TIME, message)
            putExtra(MESSAGE_WORKOUT, workoutIndex)
        }
        startActivity(intent);
    }
}
