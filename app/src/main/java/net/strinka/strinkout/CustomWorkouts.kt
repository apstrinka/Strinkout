package net.strinka.strinkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView

class CustomWorkouts : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_workouts)

        updateCustomWorkouts()
    }

    fun newCustomWorkoutsButton(view: View){
        val intent = Intent(this, NewCustomWorkout::class.java)
        startActivity(intent)
    }

    fun updateCustomWorkouts(){
        val listView = findViewById<ListView>(R.id.custom_workouts_list)
        val customWorkouts = readCustomWorkouts(filesDir)
        val array = customWorkouts.map { i -> i.name }.toTypedArray()
        listView.adapter = ArrayAdapter<String>(this, R.layout.array_adapter_test, array)
    }
}
