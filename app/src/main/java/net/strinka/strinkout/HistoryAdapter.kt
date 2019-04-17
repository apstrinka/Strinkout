package net.strinka.strinkout

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView

public class HistoryAdapter : ArrayAdapter<List<String>>{
    private val resource: Int
    private val objects: List<List<String>>

    constructor(context: Context, resource: Int, objects: List<List<String>>) : super(context, resource, objects){
        this.resource = resource
        this.objects = objects
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val row = objects[position]
        val view = LinearLayout(context)
        view.orientation = LinearLayout.HORIZONTAL
        view.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        //val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        for (column in row){
            //val textView = inflater.inflate(resource) as TextView
            val textView = TextView(context)
            textView.setTextColor(Color.WHITE)
            textView.text = column
            textView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            view.addView(textView)
        }
        return view
    }
}