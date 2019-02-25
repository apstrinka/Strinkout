package net.strinka.strinkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

const val EXTRA_MESSAGE = "net.strinka.strinkout.MESSAGE"

class SelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)
    }

    fun beginWorkout(view: View) {
        val time = findViewById<EditText>(R.id.select_time)
        val message = time.text.toString()
        val intent = Intent(this, WorkoutActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent);
    }
}
