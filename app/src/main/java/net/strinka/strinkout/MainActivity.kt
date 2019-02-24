package net.strinka.strinkout

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
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

        val initListener = TextToSpeech.OnInitListener {
            fun onInit(status: Int) {
                if (status != TextToSpeech.ERROR) {
                    tts?.setLanguage(Locale.UK);
                    tts?.setSpeechRate(0.8f)
                }
            }
        }

        tts = TextToSpeech(getApplicationContext(), initListener)
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
            R.id.action_settings -> selectSettingsItem()
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun selectSettingsItem(): Boolean{
        Log.d("MyTag", "Select Settings")
        return true
    }

    fun selectWorkout(view: View) {
        val intent = Intent(this, SelectionActivity::class.java)
        startActivity(intent);
    }

    fun testSpeech(view: View) {
        val editText = findViewById<EditText>(R.id.speech_test)
        val toSpeak = editText.getText().toString()
        Toast.makeText(applicationContext, toSpeak, Toast.LENGTH_SHORT).show()
        tts?.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null)
    }


}
