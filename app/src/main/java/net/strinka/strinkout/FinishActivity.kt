package net.strinka.strinkout

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FinishActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        val workoutMillis = intent.getStringExtra(MESSAGE_COMPLETED).toLong()
        findViewById<TextView>(R.id.summary).text = summaryText(workoutMillis)
    }

    fun backClick(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun summaryText(millis: Long): String{
        val min = millis / 60000
        val sec = (millis % 60000) / 1000
        var ret = "You worked out for "
        if (min == 1L){
            ret += "1 minute"
        } else {
            ret += "$min minutes"
        }
        if (sec == 1L){
            ret += " and 1 second"
        } else if (sec > 1) {
            ret += " and $sec seconds"
        }
        ret += "!"
        return ret
    }
}
