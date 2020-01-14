package net.strinka.strinkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LowerBodyWorkoutsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lower_body_workouts)
    }

    fun selectBeginner(view: View){
        selectWorkout(this, 11)
    }

    fun selectStandard(view: View){
        selectWorkout(this, 12)
    }

    fun selectAdvanced(view: View){
        selectWorkout(this, 13)
    }
}
