package net.strinka.strinkout

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.util.*

class WorkoutActivity : AppCompatActivity() {
    private var tts : TextToSpeech? = null;
    private var exerciseMillis: Long = 30000
    private var transitionMillis: Long = 5000
    private var hasRests = true
    private var restSeconds: Int = 30
    private var restMillis: Long = 30000
    private var restAfter = 5
    private var restsCount = false
    private var currentTimer: PausableTimer? = null
    private var totalTimeLeft: Long = 0
    private var exerciseIndex = 0
    private var exercisesSinceRest = 0
    private var currentActivityType = ActivityType.TRANSITION
    private var currentActivityDuration: Long = 0
    private var currentExercise = Exercise("Test", false)
    private var nextExercise = Exercise("Test", false)
    private var exerciseList: MutableList<Exercise> = arrayListOf()
    private var shuffle = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        val imageView = findViewById<ImageView>(R.id.workout_animation)
        imageView.setBackgroundResource(R.drawable.test_animation)
        val frameAnimation = imageView.background as AnimationDrawable
        frameAnimation.start()

        var preferences = PreferenceManager.getDefaultSharedPreferences(this)
        exerciseMillis = Math.max(preferences.getInt("exercise_seconds", 30).toLong(), 10)*1000
        transitionMillis = Math.max(preferences.getInt("transition_seconds", 5).toLong(), 1)*1000
        hasRests = preferences.getBoolean("has_rests", true)
        restSeconds =  Math.max(preferences.getInt("rest_seconds", 30), 5)
        restMillis = restSeconds.toLong()*1000
        restAfter = Math.max(preferences.getInt("rest_after", 5), 1)
        restsCount = preferences.getBoolean("rests_count", false)
        if (!hasRests)
            restAfter = Int.MAX_VALUE
        shuffle = preferences.getBoolean("shuffle_exercises", true)

        val initListener = TextToSpeech.OnInitListener {
            fun onInit(status: Int) {
                if (status != TextToSpeech.ERROR) {
                    tts?.setLanguage(Locale.US);
                    tts?.setSpeechRate(0.6f)
                }
            }
        }
        tts = TextToSpeech(this, initListener)
        val message = intent.getStringExtra(MESSAGE_TIME)
        totalTimeLeft = message.toLong()*60000
        findViewById<TextView>(R.id.total_time_remaing).text = millisToString(totalTimeLeft)

        var workoutIndex = intent.getIntExtra(MESSAGE_WORKOUT, 0)
        exerciseList = defaultWorkouts[workoutIndex].exercises.toMutableList()
        if (shuffle) {
            exerciseList.shuffle()
        }
        exerciseIndex = 0
        exercisesSinceRest = 0
        currentActivityType = ActivityType.TRANSITION
        nextExercise = exerciseList[0]
        findViewById<TextView>(R.id.next_exercise).text = nextExercise.name

    }

    override fun onStop() {
        currentTimer?.pause()
        super.onStop()
    }

    fun start(view: View) {
        if (currentTimer == null) {
            tts?.speak("Welcome back.", TextToSpeech.QUEUE_ADD, null)
            startTransition()
        }
        currentTimer?.start()
        findViewById<Button>(R.id.start_button).isEnabled = false
        findViewById<Button>(R.id.pause_button).isEnabled = true
        findViewById<Button>(R.id.stop_button).isEnabled = true
        findViewById<Button>(R.id.next_button).isEnabled = true
    }

    fun pause(view: View){
        currentTimer?.pause()
        findViewById<Button>(R.id.start_button).isEnabled = true
        findViewById<Button>(R.id.pause_button).isEnabled = false
    }

    fun stop(view: View){
        pause(view)

        val clickListener = object : DialogInterface.OnClickListener{
            override fun onClick(dialogInterface: DialogInterface, which: Int){
                finishWorkout()
            }
        }

        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to stop?")
            .setPositiveButton("Yes", clickListener)
            .setNegativeButton("No", null)
            .show()
    }

    fun next(view: View){
        currentTimer!!.pause()
        val elapsed = currentTimer!!.getCurrentElapsed()
        if (doesCurrentActivityCount()){
            totalTimeLeft -= elapsed
            findViewById<TextView>(R.id.total_time_remaing).text = millisToString(totalTimeLeft)
        }

        if (currentActivityType == ActivityType.TRANSITION){
            updateNextExercise()
            findViewById<TextView>(R.id.next_exercise).text = nextExercise.name
        }

        startTransition()
    }

    private fun millisToString(millis: Long) : String{
        val min = millis / 60000
        val sec = (millis % 60000) / 1000
        val cen = (millis % 1000) / 10
        if (min > 0)
            return "$min:${sec.toString().padStart(2, '0')}.${cen.toString().padStart(2, '0')}"
        else
            return "$sec.${cen.toString().padStart(2, '0')}"
    }

    private fun doesCurrentActivityCount() : Boolean{
        return when (currentActivityType){
            ActivityType.TRANSITION -> false
            ActivityType.EXERCISE -> true
            ActivityType.REST -> restsCount
            ActivityType.FINISHED -> false
        }
    }

    private val onTick = fun(millisElapsed: Long){
        var millisLeft = currentActivityDuration - millisElapsed
        findViewById<TextView>(R.id.current_time_remaing).text = millisToString(millisLeft)
        if (doesCurrentActivityCount()){
            var totalMillisLeft = totalTimeLeft - millisElapsed
            findViewById<TextView>(R.id.total_time_remaing).text = millisToString(totalMillisLeft)
        }
    }

    private val onFinish = fun(){
        if (doesCurrentActivityCount()){
            totalTimeLeft -= currentActivityDuration
            findViewById<TextView>(R.id.total_time_remaing).text = millisToString(totalTimeLeft)
            if (totalTimeLeft <= 0){
                finishWorkout()
                return
            }
        }
        when(currentActivityType){
            ActivityType.TRANSITION -> {
                startNextExercise()
            }
            ActivityType.EXERCISE -> {
                if (exercisesSinceRest >= restAfter){
                    startRest()
                } else {
                    startTransition()
                }
            }
            ActivityType.REST -> {
                startTransition()
            }
        }
    }

    private val switchSideTransition = fun(){
        totalTimeLeft -= currentActivityDuration
        findViewById<TextView>(R.id.total_time_remaing).text = millisToString(totalTimeLeft)
        currentActivityType = ActivityType.TRANSITION
        currentActivityDuration = transitionMillis
        findViewById<TextView>(R.id.current_activity).text = "Switch sides"
        tts?.speak("Switch sides", TextToSpeech.QUEUE_ADD, null)
        currentTimer = PausableTimer(30, currentActivityDuration, onTick, switchSideExercise)
        currentTimer?.addEvent(currentActivityDuration - 1000, speak("Ready"))
        currentTimer?.start()
    }

    private val switchSideExercise = fun(){
        currentActivityType = ActivityType.EXERCISE
        currentActivityDuration = Math.min(exerciseMillis/2, totalTimeLeft)
        findViewById<TextView>(R.id.current_activity).text = currentExercise.name
        tts?.speak("Begin", TextToSpeech.QUEUE_ADD, null)
        currentTimer = PausableTimer(30, currentActivityDuration, onTick, onFinish)
        if (exercisesSinceRest < restAfter && totalTimeLeft - currentActivityDuration > 0) {
            currentTimer?.addEvent(currentActivityDuration - 10000, speak("Next exercise. ${nextExercise.name}"))
        }
        addCountdown(3)
        currentTimer?.start()
    }

    private fun speak(text: String): () -> Unit{
        return {
            tts?.speak(text, TextToSpeech.QUEUE_ADD, null)
        }
    }

    private fun addCountdown(seconds: Int){
        for (i in 1..seconds){
            currentTimer?.addEvent(currentActivityDuration - i*1000, speak(i.toString()))
        }
    }

    private fun startTransition(){
        currentActivityType = ActivityType.TRANSITION
        currentActivityDuration = transitionMillis
        findViewById<TextView>(R.id.current_activity).text = "Transition"
        tts?.speak(nextExercise.name, TextToSpeech.QUEUE_ADD, null)
        currentTimer = PausableTimer(30, currentActivityDuration, onTick, onFinish)
        currentTimer?.addEvent(currentActivityDuration - 1000, speak("Ready"))
        currentTimer?.start()
    }

    private fun updateNextExercise(){
        exerciseIndex += 1
        if (exerciseIndex >= exerciseList.size){
            exerciseIndex = 0
            if (shuffle) {
                exerciseList.shuffle()
            }
        }
        nextExercise = exerciseList[exerciseIndex]
    }

    private fun startNextExercise(){
        if (nextExercise.sided){
            currentActivityType = ActivityType.EXERCISE
            currentActivityDuration = Math.min(exerciseMillis, totalTimeLeft)/2
            currentExercise = nextExercise
            updateNextExercise()
            exercisesSinceRest += 1

            findViewById<TextView>(R.id.current_activity).text = currentExercise.name

            if (totalTimeLeft - currentActivityDuration <= 0) {
                findViewById<TextView>(R.id.next_exercise).text = "Almost done!"
            } else if (exercisesSinceRest >= restAfter) {
                findViewById<TextView>(R.id.next_exercise).text = "Rest"
            } else {
                findViewById<TextView>(R.id.next_exercise).text = nextExercise.name
            }

            tts?.speak("Begin", TextToSpeech.QUEUE_ADD, null)
            currentTimer = PausableTimer(30, currentActivityDuration, onTick, switchSideTransition)
            currentTimer?.start()
        } else {
            currentActivityType = ActivityType.EXERCISE
            currentActivityDuration = Math.min(exerciseMillis, totalTimeLeft)
            currentExercise = nextExercise
            updateNextExercise()
            exercisesSinceRest += 1

            findViewById<TextView>(R.id.current_activity).text = currentExercise.name

            if (totalTimeLeft - currentActivityDuration <= 0) {
                findViewById<TextView>(R.id.next_exercise).text = "Almost done!"
            } else if (exercisesSinceRest >= restAfter) {
                findViewById<TextView>(R.id.next_exercise).text = "Rest"
            } else {
                findViewById<TextView>(R.id.next_exercise).text = nextExercise.name
            }

            tts?.speak("Begin", TextToSpeech.QUEUE_ADD, null)
            currentTimer = PausableTimer(30, currentActivityDuration, onTick, onFinish)
            if (exercisesSinceRest < restAfter && totalTimeLeft - currentActivityDuration > 0) {
                currentTimer?.addEvent(currentActivityDuration - 10000, speak("Next exercise. ${nextExercise.name}"))
            }
            addCountdown(3)
            currentTimer?.start()
        }
    }

    private fun startRest(){
        currentActivityType = ActivityType.REST
        currentActivityDuration = restMillis
        findViewById<TextView>(R.id.current_activity).text = "Rest"
        findViewById<TextView>(R.id.next_exercise).text = nextExercise.name
        if (restsCount && totalTimeLeft <= restMillis){
            currentActivityDuration = totalTimeLeft
            findViewById<TextView>(R.id.next_exercise).text = "Almost done!"
        }

        exercisesSinceRest = 0
        tts?.speak("Rest for $restSeconds seconds", TextToSpeech.QUEUE_ADD, null)
        currentTimer = PausableTimer(30, currentActivityDuration, onTick, onFinish)
        if (totalTimeLeft - currentActivityDuration > 0) {
            currentTimer?.addEvent(currentActivityDuration - 10000, speak("Next exercise. $nextExercise"))
        }
        addCountdown(3)
        currentTimer?.start()
    }

    private fun finishWorkout(){
        tts?.speak("Congrats! You've finished!", TextToSpeech.QUEUE_ADD, null)
        findViewById<TextView>(R.id.current_time_remaing).text = millisToString(0)
        findViewById<TextView>(R.id.current_activity).text = "Congrats!"
        findViewById<TextView>(R.id.next_exercise).text = "You've Finished!"
        findViewById<Button>(R.id.start_button).isEnabled = false
        findViewById<Button>(R.id.pause_button).isEnabled = false
        findViewById<Button>(R.id.stop_button).isEnabled = false
        findViewById<Button>(R.id.next_button).isEnabled = false
    }

    private fun endWorkout(){

    }
}
