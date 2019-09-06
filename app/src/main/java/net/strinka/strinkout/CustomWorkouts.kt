package net.strinka.strinkout

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomWorkouts : AppCompatActivity() {

    private lateinit var customWorkoutViewModel: CustomWorkoutViewModel
    private lateinit var adapter: StringListAdapter
    private val itemTouchHelper by lazy {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
            0
        ){
            var dragFrom = -1
            var dragTo = -1

            override fun isLongPressDragEnabled(): Boolean {
                return true
            }

            override fun isItemViewSwipeEnabled(): Boolean {
                return false
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)

                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    if (viewHolder?.adapterPosition != null) {
                        adapter.select(viewHolder.adapterPosition)
//                        adapter.selectedItem = viewHolder.adapterPosition
//                        (viewHolder as StringListAdapter.ViewHolder).stringItemView.setTextColor(
//                            Color.RED)
                    }
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)

                if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo)
                    customWorkoutViewModel.moveItem(dragFrom, dragTo)
                dragFrom = -1
                dragTo = -1
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                val adapter = recyclerView.adapter as StringListAdapter
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition

                adapter.selectedItem = to

                if (dragFrom == -1)
                    dragFrom = from
                dragTo = to

                adapter.notifyItemMoved(from, to)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        ItemTouchHelper(simpleItemTouchCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_workouts)

        customWorkoutViewModel = ViewModelProviders.of(this).get(CustomWorkoutViewModel::class.java)



        val recyclerView = findViewById<RecyclerView>(R.id.custom_workouts_recycler_view)
        adapter = StringListAdapter(this, recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        customWorkoutViewModel.allCustomWorkouts.observe(this, Observer { customWorkouts ->
            adapter.setStrings(customWorkouts.map { it.name } )
        })
    }

    fun newCustomWorkoutButton(view: View){
        val intent = Intent(this, NewCustomWorkout::class.java)
        startActivityForResult(intent, newCustomWorkoutRequestCode)
    }

    fun removeSelectedWorkoutButton(view: View){
        val index = adapter.selectedItem
        if (index < 0)
            return;

        val selected = customWorkoutViewModel.allCustomWorkouts.value?.get(index)
        customWorkoutViewModel.remove(selected!!.customWorkoutId)
        adapter.unselect()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newCustomWorkoutRequestCode && resultCode == Activity.RESULT_OK){
            data?.let {
                val name = it.getStringExtra(NewCustomWorkout.EXTRA_REPLY)
                customWorkoutViewModel.insert(name)
            }
        }
    }

    companion object {
        const val newCustomWorkoutRequestCode = 1
    }
}
