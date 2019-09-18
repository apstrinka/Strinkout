package net.strinka.strinkout

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

import java.io.File
import java.util.*

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        updateHistory()
    }

    private fun updateHistory(){
        val listView = findViewById<ListView>(R.id.activity_history_list)
        val graphView = findViewById<GraphView>(R.id.history_graph)
        val history = History(filesDir)
        listView.adapter = HistoryAdapter(this, R.layout.array_adapter_test, history.getRecords())
        graphView.barValues = history.getGraphValues()
    }

    fun clearHistoryButton(view: View){
        showConfirmDialog(this, "Are you sure you want to delete your workout history?", clearHistory)
    }

    private val clearHistory = fun(){
        val file = File(filesDir, historyFilename)
        file.delete()
        updateHistory()
    }

    fun testButton(view: View){
        val file = File(filesDir, historyFilename)
        if (!file.exists())
            file.createNewFile()
        file.appendText("0,0,Test Name\n")
        updateHistory()
    }

    fun switchHistoryView(view: View){
        val button = findViewById<Button>(R.id.switch_history_button)
        val listView = findViewById<LinearLayout>(R.id.history_list)
        val graphView = findViewById<GraphView>(R.id.history_graph)
        if (listView.visibility == View.VISIBLE){
            button.text = "View as List"
            listView.visibility = View.GONE
            graphView.visibility = View.VISIBLE
        } else {
            button.text = "View as Graph"
            listView.visibility = View.VISIBLE
            graphView.visibility = View.GONE
        }
    }
}
