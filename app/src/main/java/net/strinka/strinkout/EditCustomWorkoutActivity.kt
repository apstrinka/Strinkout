package net.strinka.strinkout

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EditCustomWorkoutActivity : AppCompatActivity() {
    private lateinit var editCustomWorkoutViewModel: EditCustomWorkoutViewModel
    private val exercises = emptyList<Exercise>().toMutableList()
    private lateinit var recyclerViewAdapter: StringListAdapter
    private var customWorkoutId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_custom_workout)

        customWorkoutId = intent.getIntExtra(MESSAGE_CUSTOM_WORKOUT_ID, 0)
        editCustomWorkoutViewModel = ViewModelProviders.of(this).get(EditCustomWorkoutViewModel::class.java)

        val spinner = findViewById<Spinner>(R.id.add_exercise_spinner)
        val array = allExercises.values.toTypedArray()
        spinner.adapter = ArrayAdapter<Exercise>(this, R.layout.array_adapter_test, array)

        val recyclerView = findViewById<RecyclerView>(R.id.custom_workout_exercise_recycler_view)
        recyclerViewAdapter = StringListAdapter(this, recyclerView)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        exercises.clear()

        if (customWorkoutId != 0) {
            editCustomWorkoutViewModel.getWorkout(customWorkoutId).invokeOnCompletion {
                runOnUiThread {
                    val workout = editCustomWorkoutViewModel.workout
                    if (workout != null)
                        findViewById<EditText>(R.id.new_workout_name).setText(workout.name)
                }
            }
            editCustomWorkoutViewModel.getExercises(customWorkoutId).invokeOnCompletion {
                runOnUiThread {
                    exercises.addAll(editCustomWorkoutViewModel.exercises)
                    recyclerViewAdapter.setStrings(exercises.map { it.toString() })
                }
            }
        }
    }

    fun addExerciseButton(view: View) {
        val spinner = findViewById<Spinner>(R.id.add_exercise_spinner)
        val item = spinner.selectedItem as Exercise

        if (item != null) {

            exercises.add(item)
            recyclerViewAdapter.setStrings(exercises.map { it.toString() })
        }
    }

    fun deleteSelectedExerciseButton(view: View){
        val selectedIndex = recyclerViewAdapter.selectedItem
        if (selectedIndex < 0)
            return

        exercises.removeAt(selectedIndex)
        recyclerViewAdapter.unselect()
        recyclerViewAdapter.setStrings(exercises.map { it.toString() })
    }

    fun finishButton(view: View){
        val nameTextView = findViewById<EditText>(R.id.new_workout_name)
        val name = nameTextView.text.toString()
        val errors = emptyList<String>().toMutableList()

        if (name == "")
            errors.add("Name cannot be empty")

        if (exercises.size == 0)
            errors.add("There must be at least one exercise")

        if (errors.size > 0){
            val errorString = errors.joinToString(separator = "\n") { it }
            val toast = Toast.makeText(this, errorString, Toast.LENGTH_LONG)
            toast.show()
            return
        }

        val workout = Workout(name, exercises.toList())
        if(customWorkoutId == 0) {
            editCustomWorkoutViewModel.createWorkout(workout)
        } else {
            editCustomWorkoutViewModel.updateWorkout(customWorkoutId, workout)
        }

        val replyIntent = Intent()
        setResult(Activity.RESULT_OK, replyIntent)
        finish()
    }

    fun cancelButton(view: View) {
        val replyIntent = Intent()
        setResult(Activity.RESULT_CANCELED, replyIntent)
        finish()
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}
