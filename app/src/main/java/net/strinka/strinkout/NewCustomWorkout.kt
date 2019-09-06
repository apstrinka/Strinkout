package net.strinka.strinkout

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText

class NewCustomWorkout : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_custom_workout)
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

    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}
