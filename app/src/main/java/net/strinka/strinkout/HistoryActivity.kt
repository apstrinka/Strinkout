package net.strinka.strinkout

import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity;

import java.io.File
import java.util.*

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
        listView.adapter = HistoryAdapter(this, R.layout.array_adapter_test, array)
    }

    private fun getRecords(): List<List<String>>{
        val file = File(filesDir, historyFilename)
        if (!file.exists())
            return listOf(listOf("No workouts recorded"))

        val lines = file.bufferedReader().readLines()
        if (lines.isEmpty())
            return listOf(listOf("No workouts recorded"))
        return lines.map{getRecord(it)}
    }

    private fun getRecord(str: String): List<String>{
        val split = str.split(',', limit = 3)
        val calendarTime = split[0].toLong()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = calendarTime
        val calendarString = "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH)}-${calendar.get(Calendar.DAY_OF_MONTH)}"
        val durationTime = split[1].toLong()
        val durationString = millisToString(durationTime)
        return listOf(calendarString, split[2], durationString)
    }

    fun clearHistoryButton(view: View){
        showConfirmDialog(this, "Are you sure you want to delete your workout history?", clearHistory)
    }

    private val clearHistory = fun(){
        val file = File(filesDir, historyFilename)
        file.delete()
        updateHistory()
    }

    public fun testButton(view: View){
        val file = File(filesDir, historyFilename)
        if (!file.exists())
            file.createNewFile()
        file.appendText("0,0,Test Name\n")
        updateHistory()
    }
}
