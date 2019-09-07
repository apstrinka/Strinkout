package net.strinka.strinkout

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*

class NewCustomWorkoutActivity : AppCompatActivity() {
    private val selected = emptyList<String>().toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_custom_workout)

        val spinner = findViewById<Spinner>(R.id.add_exercise_spinner)
        val array = allExercises.values.map { it.name }.toTypedArray()
        spinner.adapter = ArrayAdapter<String>(this, R.layout.array_adapter_test, array)
    }

    fun finishButton(view: View){
        val textView = findViewById<EditText>(R.id.new_workout_name)
        val replyIntent = Intent()
        if (TextUtils.isEmpty(textView.text)){
            setResult(Activity.RESULT_CANCELED, replyIntent)
        } else {
            val name = textView.text.toString()
            replyIntent.putExtra(EXTRA_REPLY, name)
            setResult(Activity.RESULT_OK, replyIntent)
        }
        finish()
    }

    fun addExerciseButton(view: View) {
        val spinner = findViewById<Spinner>(R.id.add_exercise_spinner)
        val item = spinner.selectedItem

        if (item != null) {

            selected.add(item.toString())
            val array = selected.toTypedArray()

            val list = findViewById<ListView>(R.id.custom_workout_exercise_list)
            list.adapter = ArrayAdapter(this, R.layout.array_adapter_test, array)
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}
