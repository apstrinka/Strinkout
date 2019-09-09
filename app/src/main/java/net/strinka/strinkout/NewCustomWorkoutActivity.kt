package net.strinka.strinkout

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NewCustomWorkoutActivity : AppCompatActivity() {
    private lateinit var customWorkoutExerciseViewModel: CustomWorkoutExerciseViewModel
    private val selected = emptyList<Exercise>().toMutableList()
    private lateinit var recyclerViewAdapter: StringListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_custom_workout)

        customWorkoutExerciseViewModel = ViewModelProviders.of(this).get(CustomWorkoutExerciseViewModel::class.java)

        val spinner = findViewById<Spinner>(R.id.add_exercise_spinner)
        val array = allExercises.values.toTypedArray()
        spinner.adapter = ArrayAdapter<Exercise>(this, R.layout.array_adapter_test, array)

        val recyclerView = findViewById<RecyclerView>(R.id.custom_workout_exercise_recycler_view)
        recyclerViewAdapter = StringListAdapter(this, recyclerView)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    fun finishButton(view: View){
        val nameTextView = findViewById<EditText>(R.id.new_workout_name)
        val name = nameTextView.text.toString()
        val errors = emptyList<String>().toMutableList()

        if (name == "")
            errors.add("Name cannot be empty")

        if (selected.size == 0)
            errors.add("There must be at least one exercise")

        if (errors.size > 0){
            val errorString = errors.joinToString(separator = "\n") { it }
            val toast = Toast.makeText(this, errorString, Toast.LENGTH_LONG)
            toast.show()
            return
        }

        val workout = Workout(name, selected.toList())
        customWorkoutExerciseViewModel.createWorkout(workout)

        val replyIntent = Intent()
        replyIntent.putExtra(EXTRA_REPLY, name)
        setResult(Activity.RESULT_OK, replyIntent)
        finish()
    }

    fun addExerciseButton(view: View) {
        val spinner = findViewById<Spinner>(R.id.add_exercise_spinner)
        val item = spinner.selectedItem as Exercise

        if (item != null) {

            selected.add(item)
            recyclerViewAdapter.setStrings(selected.map { it.toString() })
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}
