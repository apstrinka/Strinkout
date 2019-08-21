package net.strinka.strinkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class NewCustomWorkout : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_custom_workout)
    }

    fun finishButton(view: View){
        val name = findViewById<EditText>(R.id.new_workout_name).text.toString();
        val newWorkout = Workout(name, emptyList())
        writeCustomWorkout(filesDir, newWorkout)

        val intent = Intent(this, CustomWorkouts::class.java)
        startActivity(intent)
    }
}
