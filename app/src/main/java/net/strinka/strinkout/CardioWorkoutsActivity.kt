package net.strinka.strinkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class CardioWorkoutsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cardio_workouts)
    }

    fun selectBeginner(view: View){
        selectWorkout(this, 14)
    }

    fun selectStandard(view: View){
        selectWorkout(this, 15)
    }

    fun selectAdvanced(view: View){
        selectWorkout(this, 16)
    }
}
