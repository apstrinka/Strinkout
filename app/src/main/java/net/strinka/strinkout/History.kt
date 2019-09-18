package net.strinka.strinkout

import android.util.Log
import java.io.File
import java.util.*

const val historyFilename = "WorkoutHistory.csv"

fun parseRecord(str: String): Record{
    val split = str.split(',', limit = 3)
    val calendarTime = split[0].toLong()
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = calendarTime
    val duration = split[1].toLong()
    return Record(calendar, split[2], duration)
}

class Record {
    val calendar: Calendar
    val name: String
    val workoutDuration: Long

    constructor(calendar: Calendar, name: String, workoutDuration: Long){
        this.calendar = calendar
        this.name = name
        this.workoutDuration = workoutDuration
    }

    fun calendarString(): String{
        return "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH)+1}-${calendar.get(Calendar.DAY_OF_MONTH)}"
    }

    fun durationString(): String{
        return millisToString(workoutDuration)
    }

    fun toListOfStrings(): List<String>{
        return listOf(calendarString(), name, durationString())
    }
}

class History{
    val filesDir: File
    var loaded = false
    private var records: List<Record> = listOf()

    constructor(filesDir: File){
        this.filesDir = filesDir
    }

    fun readRecords(): List<Record>{
        val file = File(filesDir, historyFilename)
        if (!file.exists())
            return emptyList()

        val lines = file.bufferedReader().readLines()
        return lines.map{parseRecord(it)}
    }

    private fun loadRecords(){
        if (!loaded) {
            loaded = true
            records = readRecords()
        }
    }

    fun forceReload(){
        loaded = false
        loadRecords()
    }

    fun getRecords(): List<Record>{
        loadRecords()
        return records
    }

    fun getGraphValues(): DoubleArray{
        loadRecords()
        if (records.isEmpty())
            return DoubleArray(0)
        val list: MutableList<Long> = mutableListOf()
        val minCal = records.map{ it.calendar }.min()!!
        val cal = Calendar.getInstance()
        cal.timeInMillis = minCal.timeInMillis
        val now = Calendar.getInstance()

        while (cal < now || isSameDay(cal, now)){
            list.add(records.filter{isSameDay(cal, it.calendar)}.map{it.workoutDuration}.sum())
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }

        return list.map { it.toDouble() / 60000 }.toDoubleArray()
    }

    private fun isSameDay(first: Calendar, second: Calendar): Boolean{
        val firstYear = first.get(Calendar.YEAR)
        val firstDay = first.get(Calendar.DAY_OF_YEAR)
        val secondYear = second.get(Calendar.YEAR)
        val secondDay = second.get(Calendar.DAY_OF_YEAR)

        return firstYear == secondYear && firstDay == secondDay
    }
}

fun writeHistory(filesDir: File, workoutName: CharSequence, workoutDuration: Long){
    if (workoutDuration > 1000) {
        val historyFile = File(filesDir, historyFilename)
        if (!historyFile.exists())
            historyFile.createNewFile()

        val time = Calendar.getInstance().timeInMillis

        historyFile.appendText("$time,$workoutDuration,$workoutName\n")
    }
}