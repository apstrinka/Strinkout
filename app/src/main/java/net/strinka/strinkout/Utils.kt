package net.strinka.strinkout

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

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