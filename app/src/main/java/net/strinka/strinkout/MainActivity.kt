package net.strinka.strinkout

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

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
        startActivity(intent)
        return true
    }

    fun selectHistoryItem(): Boolean{
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
        return true
    }

    fun selectStandard(view: View){
        selectWorkout(this, 2)
    }

    fun selectBeginner(view: View){
        selectWorkout(this, 1)
    }

    fun selectAdvanced(view: View){
        selectWorkout(this, 3)
    }

    fun selectQuiet(view: View){
        selectWorkout(this, 4)
    }

    fun selectUpperBody(view: View){
        val intent = Intent(this, UpperBodyWorkoutsActivity::class.java)
        startActivity(intent)
    }

    fun selectCore(view: View){
        val intent = Intent(this, CoreWorkoutsActivity::class.java)
        startActivity(intent)
    }

    fun selectLowerBody(view: View){
        val intent = Intent(this, LowerBodyWorkoutsActivity::class.java)
        startActivity(intent)
    }

    fun selectCardio(view: View){
        val intent = Intent(this, CardioWorkoutsActivity::class.java)
        startActivity(intent)
    }

    fun customWorkoutsButton(view: View){
        val intent = Intent(this, CustomWorkoutsActivity::class.java)
        startActivity(intent)
    }
}
