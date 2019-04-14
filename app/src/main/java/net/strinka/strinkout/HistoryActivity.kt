package net.strinka.strinkout

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter

val historyFilename = "WorkoutHistory.csv"

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        updateHistory()
    }

    private fun updateHistory(){
        val listView = findViewById<ListView>(R.id.activity_history_list)
        val array = getRecords()
        listView.adapter = ArrayAdapter<String>(this, R.layout.array_adapter_test, array)
    }

    private fun getRecords(): Array<String>{
        Log.d("mytag", "getRecords")
        val file = File(filesDir, historyFilename)
        if (!file.exists())
            return arrayOf("No workouts recorded")
        Log.d("mytag", "getRecords file exists")

        val lines = file.bufferedReader().readLines()
        if (lines.isEmpty())
            return arrayOf("No workouts recorded")
        return lines.toTypedArray()
    }

    fun clearHistoryButton(view: View){
        val yesClickListener = DialogInterface.OnClickListener { dialogInterface, which ->
            clearHistory()
        }

        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to delete your workout history?")
            .setPositiveButton("Yes", yesClickListener)
            .setNegativeButton("No", null)
            .show()
    }

    private fun clearHistory(){
        val file = File(filesDir, historyFilename)
        file.delete()
        updateHistory()
    }

    public fun testButton(view: View){
        val file = File(filesDir, historyFilename)
        if (!file.exists())
            file.createNewFile()
        file.appendText("Test\n")
        updateHistory()
    }
}
