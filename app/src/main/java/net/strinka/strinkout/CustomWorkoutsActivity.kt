package net.strinka.strinkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

const val MESSAGE_CUSTOM_WORKOUT_ID = "net.strinka.strinkout.MESSAGE_CUSTOM_WORKOUT_ID"

class CustomWorkoutsActivity : AppCompatActivity() {

    private lateinit var customWorkoutsViewModel: CustomWorkoutsViewModel
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
                    customWorkoutsViewModel.moveItem(dragFrom, dragTo)
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

        customWorkoutsViewModel = ViewModelProviders.of(this).get(CustomWorkoutsViewModel::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.custom_workouts_recycler_view)
        adapter = StringListAdapter(this, recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        customWorkoutsViewModel.allCustomWorkouts.observe(this, Observer { customWorkouts ->
            adapter.setStrings(customWorkouts.map { it.name } )
        })
    }

    fun newCustomWorkoutButton(view: View){
        val intent = Intent(this, EditCustomWorkoutActivity::class.java)
        intent.putExtra(MESSAGE_CUSTOM_WORKOUT_ID, 0)
        startActivityForResult(intent, newCustomWorkoutRequestCode)
    }

    fun editSelectedWorkoutButton(view: View){
        val index = adapter.selectedItem
        if (index < 0)
            return

        val selected = customWorkoutsViewModel.allCustomWorkouts.value?.get(index)
        val intent = Intent(this, EditCustomWorkoutActivity::class.java)
        intent.putExtra(MESSAGE_CUSTOM_WORKOUT_ID, selected!!.customWorkoutId)
        startActivityForResult(intent, newCustomWorkoutRequestCode)
    }

    fun removeSelectedWorkoutButton(view: View){
        val index = adapter.selectedItem
        if (index < 0)
            return

        val selected = customWorkoutsViewModel.allCustomWorkouts.value?.get(index)
        customWorkoutsViewModel.remove(selected!!.customWorkoutId)
        adapter.unselect()
    }

    fun selectWorkoutButton(view: View){
        val index = adapter.selectedItem
        if (index < 0)
            return

        val selected = customWorkoutsViewModel.allCustomWorkouts.value?.get(index)
        val intent = Intent(this, SelectionActivity::class.java)
        intent.putExtra(MESSAGE_WORKOUT_CUSTOM, true)
        intent.putExtra(MESSAGE_WORKOUT, selected!!.customWorkoutId)
        startActivity(intent)
    }

    companion object {
        const val newCustomWorkoutRequestCode = 1
    }
}
