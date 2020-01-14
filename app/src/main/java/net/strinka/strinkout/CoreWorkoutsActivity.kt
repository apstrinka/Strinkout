package net.strinka.strinkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class CoreWorkoutsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_core_workouts)
    }

    fun selectBeginner(view: View){
        selectWorkout(this, 8)
    }

    fun selectStandard(view: View){
        selectWorkout(this, 9)
    }

    fun selectAdvanced(view: View){
        selectWorkout(this, 10)
    }
}
