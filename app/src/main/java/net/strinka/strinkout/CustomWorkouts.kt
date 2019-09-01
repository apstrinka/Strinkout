package net.strinka.strinkout

import android.app.Activity
import android.content.Intent
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_workouts)

        customWorkoutViewModel = ViewModelProviders.of(this).get(CustomWorkoutViewModel::class.java)

        adapter = StringListAdapter(this)

        val recyclerView = findViewById<RecyclerView>(R.id.custom_workouts_recycler_view)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        customWorkoutViewModel.allCustomWorkouts.observe(this, Observer { customWorkouts ->
            Log.d("mytag", "customWorkoutsObserver {" + customWorkouts.size.toString() + "}")
            adapter.setStrings(customWorkouts.map { it.name } )
        })

        //updateCustomWorkouts()
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

//    fun updateCustomWorkouts(){
//        val listView = findViewById<ListView>(R.id.custom_workouts_list)
//        val customWorkouts = readCustomWorkouts(filesDir)
//        val array = customWorkouts.map { i -> i.name }.toTypedArray()
//        listView.adapter = ArrayAdapter<String>(this, R.layout.array_adapter_test, array)
//    }

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
