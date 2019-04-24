package net.strinka.strinkout

import java.io.File
import java.util.*

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
        return "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH)}-${calendar.get(Calendar.DAY_OF_MONTH)}"
    }

    fun durationString(): String{
        return millisToString(workoutDuration)
    }

    fun toListOfStrings(): List<String>{
        return listOf(calendarString(), name, durationString())
    }
}

const val historyFilename = "WorkoutHistory.csv"

fun writeHistory(filesDir: File, workoutName: CharSequence, workoutDuration: Long){
    if (workoutDuration > 1000) {
        val historyFile = File(filesDir, historyFilename)
        if (!historyFile.exists())
            historyFile.createNewFile()

        val time = Calendar.getInstance().timeInMillis

        historyFile.appendText("$time,$workoutDuration,$workoutName\n")
    }
}

fun getRecords(filesDir: File): List<Record>{
    val file = File(filesDir, historyFilename)
    if (!file.exists())
        return emptyList()

    val lines = file.bufferedReader().readLines()
    return lines.map{getRecord(it)}
}

fun getRecord(str: String): Record{
    val split = str.split(',', limit = 3)
    val calendarTime = split[0].toLong()
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = calendarTime
    val duration = split[1].toLong()
    return Record(calendar, split[2], duration)
}

fun convertForGraph(records: List<Record>): DoubleArray{
    return records.map { it.workoutDuration.toDouble() / 60000 }.toDoubleArray()
}