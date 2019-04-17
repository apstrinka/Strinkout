package net.strinka.strinkout

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private var tts : TextToSpeech? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.menu_settings -> selectSettingsItem()
            R.id.menu_history -> selectHistoryItem()
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun selectSettingsItem(): Boolean{
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent);
        return true
    }

    fun selectHistoryItem(): Boolean{
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent);
        return true
    }

    fun selectStandard(view: View){
        selectWorkout(0)
    }

    fun selectBeginner(view: View){
        selectWorkout(1)
    }

    fun selectAdvanced(view: View){
        selectWorkout(2)
    }

    fun selectQuiet(view: View){
        selectWorkout(3)
    }

    fun selectTest(view: View){
        selectWorkout(4)
    }

    private fun selectWorkout(workoutIndex: Int) {
        val intent = Intent(this, SelectionActivity::class.java).apply {
            putExtra(MESSAGE_WORKOUT, workoutIndex)
        }
        startActivity(intent)
    }
}
