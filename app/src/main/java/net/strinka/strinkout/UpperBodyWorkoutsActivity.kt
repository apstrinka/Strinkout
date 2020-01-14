package net.strinka.strinkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class UpperBodyWorkoutsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upper_body_workouts)
    }

    fun selectBeginner(view: View){
        selectWorkout(this, 5)
    }

    fun selectStandard(view: View){
        selectWorkout(this, 6)
    }

    fun selectAdvanced(view: View){
        selectWorkout(this, 7)
    }
}
