package net.strinka.strinkout

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView

class HistoryAdapter : ArrayAdapter<Record>{
    private val resource: Int
    private val records: List<Record>

    constructor(context: Context, resource: Int, records: List<Record>) : super(context, resource){
        this.resource = resource
        this.records = records
    }

    override fun getCount(): Int {
        return Math.max(records.size, 1)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (position >= records.size){
            val textView = TextView(context)
            textView.setTextColor(Color.WHITE)
            textView.text = "No workouts recorded"
            //textView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            return textView
        }
        val record = records[position]
        val view = LinearLayout(context)
        view.orientation = LinearLayout.HORIZONTAL
        view.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        //val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        for (column in record.toListOfStrings()){
            //val textView = inflater.inflate(resource) as TextView
            val textView = TextView(context)
            textView.setTextColor(Color.WHITE)
            textView.text = column
            //textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            textView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            view.addView(textView)
        }
        return view
    }
}