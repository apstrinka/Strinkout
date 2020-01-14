package net.strinka.strinkout

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent

fun showConfirmDialog(context: Context, message: String, onYes: ()->Unit){
    val yesClickListener = DialogInterface.OnClickListener { dialogInterface, which ->
        onYes()
    }

    AlertDialog.Builder(context)
        .setMessage(message)
        .setPositiveButton("Yes", yesClickListener)
        .setNegativeButton("No", null)
        .show()
}

fun millisToString(millis: Long) : String{
    val min = millis / 60000
    val sec = (millis % 60000) / 1000
    val cen = (millis % 1000) / 10
    if (min > 0)
        return "$min:${sec.toString().padStart(2, '0')}.${cen.toString().padStart(2, '0')}"
    else
        return "$sec.${cen.toString().padStart(2, '0')}"
}

fun selectWorkout(activity: Activity, workoutId: Int) {
    val intent = Intent(activity, SelectionActivity::class.java)
    intent.putExtra(MESSAGE_WORKOUT, workoutId)
    activity.startActivity(intent)
}