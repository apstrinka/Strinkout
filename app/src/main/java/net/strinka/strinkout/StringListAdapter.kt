package net.strinka.strinkout

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StringListAdapter internal constructor(context: Context, recyclerView: RecyclerView) : RecyclerView.Adapter<StringListAdapter.ViewHolder>() {
    private val recyclerView = recyclerView
    private val inflater = LayoutInflater.from(context)
    private var strings = emptyList<String>()
    var selectedItem = -1

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val stringItemView: TextView = itemView.findViewById<TextView>(R.id.stringView)

        init {
            itemView.setOnClickListener { _ ->
                notifyItemChanged(selectedItem)
                if (selectedItem == layoutPosition)
                    selectedItem = -1
                else
                    selectedItem = layoutPosition
                notifyItemChanged(selectedItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = strings[position]
        holder.stringItemView.text = current
        if (position == selectedItem)
            holder.stringItemView.setTextColor(Color.RED)
        else
            holder.stringItemView.setTextColor(Color.WHITE)
    }

    internal fun setStrings(strings: List<String>) {
        this.strings = strings
        notifyDataSetChanged()
    }

    override fun getItemCount() = strings.size

    fun select(index: Int){
        val prevSelected = selectedItem
        selectedItem = index
        updateTextColor(recyclerView.findViewHolderForAdapterPosition(prevSelected), false)
        updateTextColor(recyclerView.findViewHolderForAdapterPosition(selectedItem), true)
    }

    fun unselect(){
        notifyItemChanged(selectedItem)
        selectedItem = -1
    }

    private fun updateTextColor(viewHolder: RecyclerView.ViewHolder?, isSelected: Boolean) {
        if (viewHolder != null){
            viewHolder as ViewHolder
            if (isSelected)
                viewHolder.stringItemView.setTextColor(Color.RED)
            else
                viewHolder.stringItemView.setTextColor(Color.WHITE)
        }
    }
}