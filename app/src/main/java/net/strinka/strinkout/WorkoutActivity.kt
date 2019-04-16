package net.strinka.strinkout

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import java.io.File
import java.util.*

class WorkoutActivity : AppCompatActivity() {
    private var tts : TextToSpeech? = null;
    private var exerciseMillis: Long = 30000
    private var introMillis: Long = 7000
    private var transitionMillis: Long = 5000
    private var hasRests = true
    private var restSeconds: Int = 30
    private var restMillis: Long = 30000
    private var restAfter = 5
    private var restsCount = false
    private var currentTimer: PausableTimer? = null
    private var totalTime: Long = 0
    private var totalTimeLeft: Long = 0
    private var timeSpentWorkingOut: Long = 0
    private var exercisesSinceRest = 0
    private var currentActivityType = ActivityType.TRANSITION
    private var currentActivityDuration: Long = 0
    private var exerciseList: ExerciseList = ExerciseList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        val imageView = findViewById<ImageView>(R.id.workout_animation)
        imageView.setBackgroundResource(R.drawable.test_animation)
        val frameAnimation = imageView.background as AnimationDrawable
        frameAnimation.start()

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        exerciseMillis = Math.max(preferences.getInt("exercise_seconds", 30).toLong(), 10)*1000
        transitionMillis = Math.max(preferences.getInt("transition_seconds", 5).toLong(), 1)*1000
        introMillis = transitionMillis + 2000;
        hasRests = preferences.getBoolean("has_rests", true)
        restSeconds =  Math.max(preferences.getInt("rest_seconds", 30), 5)
        restMillis = restSeconds.toLong()*1000
        restAfter = Math.max(preferences.getInt("rest_after", 5), 1)
        restsCount = preferences.getBoolean("rests_count", false)
        if (!hasRests)
            restAfter = Int.MAX_VALUE
        val shuffle = preferences.getBoolean("shuffle_exercises", true)

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
        totalTime = message.toLong()*60000
        totalTimeLeft = totalTime
        updateTotalTime(totalTimeLeft);
        updateCurrentTime(introMillis);

        val workoutIndex = intent.getIntExtra(MESSAGE_WORKOUT, 0)
        val workout = defaultWorkouts[workoutIndex]
        title = workout.name
        exerciseList = ExerciseList(workout, shuffle)
        findViewById<TextView>(R.id.next_exercise).text = exerciseList.nextExercise.name
    }

    override fun onStop() {
        currentTimer?.pause()
        super.onStop()
    }

    fun start(view: View) {
        if (currentTimer == null) {
            tts?.speak("Welcome back.", TextToSpeech.QUEUE_ADD, null, "")
            currentActivityType = ActivityType.INTRO
            currentActivityDuration = transitionMillis + 2000
            findViewById<TextView>(R.id.current_activity).text = "Get ready!"
            currentTimer = PausableTimer(30, currentActivityDuration, onTick, onFinish)
            currentTimer?.addEvent(2000, speak(exerciseList.nextExercise.name))
            currentTimer?.addEvent(currentActivityDuration - 1000, speak("Ready"))
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
        
        val onYes = fun(){
            if (currentActivityType == ActivityType.EXERCISE) {
                timeSpentWorkingOut += currentTimer!!.getCurrentElapsed()
            }
            endWorkout()
        }

        showConfirmDialog(this, "Are you sure you want to stop?", onYes)
    }



    fun next(view: View){
        currentTimer!!.pause()
        val elapsed = currentTimer!!.getCurrentElapsed()

        if (currentActivityType == ActivityType.EXERCISE){
            timeSpentWorkingOut += elapsed
        }

        if (doesCurrentActivityCount()){
            totalTimeLeft -= elapsed
            updateTotalTime(totalTimeLeft);
        }

        if (currentActivityType == ActivityType.TRANSITION){
            exerciseList.moveNext()
            findViewById<TextView>(R.id.next_exercise).text = exerciseList.nextExercise.name
        }

        startTransition()
        findViewById<ProgressBar>(R.id.current_time_progress).progressBackgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorBlack))
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
            ActivityType.INTRO -> false
            ActivityType.TRANSITION -> false
            ActivityType.EXERCISE -> true
            ActivityType.REST -> restsCount
            ActivityType.FINISHED -> false
        }
    }

    private val onTick = fun(millisElapsed: Long){
        var millisLeft = currentActivityDuration - millisElapsed
        updateCurrentTime(millisLeft);
        if (currentActivityType == ActivityType.INTRO){
            var totalTimeProgressBar = findViewById<ProgressBar>(R.id.total_time_progress)
            var currentTimeProgressBar = findViewById<ProgressBar>(R.id.current_time_progress)
            totalTimeProgressBar.progress = currentTimeProgressBar.progress
        }
        if (doesCurrentActivityCount()){
            var totalMillisLeft = totalTimeLeft - millisElapsed
            updateTotalTime(totalMillisLeft);
        }
    }

    private val onFinish = fun(){
        if (currentActivityType == ActivityType.EXERCISE){
            timeSpentWorkingOut += currentActivityDuration
        }

        if (doesCurrentActivityCount()){
            totalTimeLeft -= currentActivityDuration
            updateTotalTime(totalTimeLeft);
            if (totalTimeLeft <= 0){
                finishWorkout()
                return
            }
        }
        when(currentActivityType){
            ActivityType.INTRO -> {
                updateTotalTimeColor(R.color.colorRed)
                startNextExercise()
            }
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
        updateCurrentTimeColor(R.color.colorBlue)
        timeSpentWorkingOut += currentActivityDuration
        totalTimeLeft -= currentActivityDuration
        updateTotalTime(totalTimeLeft);
        currentActivityType = ActivityType.TRANSITION
        currentActivityDuration = transitionMillis
        findViewById<TextView>(R.id.current_activity).text = "Switch sides"
        tts?.speak("Switch sides", TextToSpeech.QUEUE_ADD, null, "")
        currentTimer = PausableTimer(30, currentActivityDuration, onTick, switchSideExercise)
        currentTimer?.addEvent(currentActivityDuration - 1000, speak("Ready"))
        currentTimer?.start()
    }

    private val switchSideExercise = fun(){
        updateCurrentTimeColor(R.color.colorRed)
        currentActivityType = ActivityType.EXERCISE
        currentActivityDuration = Math.min(exerciseMillis/2, totalTimeLeft)
        findViewById<TextView>(R.id.current_activity).text = exerciseList.currentExercise.name
        tts?.speak("Begin", TextToSpeech.QUEUE_ADD, null, "")
        currentTimer = PausableTimer(30, currentActivityDuration, onTick, onFinish)
        if (exercisesSinceRest < restAfter && totalTimeLeft - currentActivityDuration > 0) {
            currentTimer?.addEvent(currentActivityDuration - 10000, speak("Next exercise. ${exerciseList.nextExercise.name}"))
        }
        addCountdown(3)
        currentTimer?.start()
    }

    private fun speak(text: String): () -> Unit{
        return {
            tts?.speak(text, TextToSpeech.QUEUE_ADD, null, "")
        }
    }

    private fun addCountdown(seconds: Int){
        for (i in 1..seconds){
            currentTimer?.addEvent(currentActivityDuration - i*1000, speak(i.toString()))
        }
    }

    private fun startTransition(){
        updateCurrentTimeColor(R.color.colorBlue)

        currentActivityType = ActivityType.TRANSITION
        currentActivityDuration = transitionMillis
        findViewById<TextView>(R.id.current_activity).text = "Transition"
        tts?.speak(exerciseList.nextExercise.name, TextToSpeech.QUEUE_ADD, null, "")
        currentTimer = PausableTimer(30, currentActivityDuration, onTick, onFinish)
        currentTimer?.addEvent(currentActivityDuration - 1000, speak("Ready"))
        currentTimer?.start()
    }

    private fun startNextExercise(){
        updateCurrentTimeColor(R.color.colorRed)

        currentActivityType = ActivityType.EXERCISE
        currentActivityDuration = Math.min(exerciseMillis, totalTimeLeft)
        exerciseList.moveNext()
        exercisesSinceRest += 1
        findViewById<TextView>(R.id.current_activity).text = exerciseList.currentExercise.name

        if (totalTimeLeft - currentActivityDuration <= 0) {
            findViewById<TextView>(R.id.next_exercise).text = "Almost done!"
        } else if (exercisesSinceRest >= restAfter) {
            findViewById<TextView>(R.id.next_exercise).text = "Rest"
        } else {
            findViewById<TextView>(R.id.next_exercise).text = exerciseList.nextExercise.name
        }

        tts?.speak("Begin", TextToSpeech.QUEUE_ADD, null, "")

        if (exerciseList.currentExercise.sided){
            currentActivityDuration = Math.min(exerciseMillis, totalTimeLeft)/2
            currentTimer = PausableTimer(30, currentActivityDuration, onTick, switchSideTransition)
        } else {
            currentTimer = PausableTimer(30, currentActivityDuration, onTick, onFinish)
            if (exercisesSinceRest < restAfter && totalTimeLeft - currentActivityDuration > 0) {
                currentTimer?.addEvent(currentActivityDuration - 10000, speak("Next exercise. ${exerciseList.nextExercise.name}"))
            }
            addCountdown(3)
        }
        currentTimer?.start()
    }

    private fun startRest(){
        updateCurrentTimeColor(R.color.colorBlack)

        currentActivityType = ActivityType.REST
        currentActivityDuration = restMillis
        findViewById<TextView>(R.id.current_activity).text = "Rest"
        findViewById<TextView>(R.id.next_exercise).text = exerciseList.nextExercise.name

        if (restsCount && totalTimeLeft <= restMillis){
            //currentActivityDuration = totalTimeLeft
            //findViewById<TextView>(R.id.next_exercise).text = "Almost done!"
            finishWorkout()
            return
        }

        exercisesSinceRest = 0
        tts?.speak("Rest for $restSeconds seconds", TextToSpeech.QUEUE_ADD, null, "")
        currentTimer = PausableTimer(30, currentActivityDuration, onTick, onFinish)
        if (totalTimeLeft - currentActivityDuration > 0) {
            currentTimer?.addEvent(currentActivityDuration - 10000, speak("Next exercise. ${exerciseList.nextExercise.name}"))
        }
        addCountdown(3)
        currentTimer?.start()
    }

    private fun updateTotalTime(millisLeft: Long){
        findViewById<TextView>(R.id.total_time_remaing).text = millisToString(millisLeft)
        findViewById<ProgressBar>(R.id.total_time_progress).progress = ((totalTime - millisLeft)*1000 / totalTime).toInt();
    }

    private fun updateCurrentTime(millisLeft: Long){
        findViewById<TextView>(R.id.current_time_remaing).text = millisToString(millisLeft)
        if (currentActivityDuration == 0L)
            findViewById<ProgressBar>(R.id.current_time_progress).progress = 0;
        else {
            val progress = ((currentActivityDuration - millisLeft)*1000 / currentActivityDuration).toInt()
            findViewById<ProgressBar>(R.id.current_time_progress).progress = progress
        }
    }

    private fun updateTotalTimeColor(colorId: Int){
        var totalTimeProgressBar = findViewById<ProgressBar>(R.id.total_time_progress)
        totalTimeProgressBar.progress = 0
        totalTimeProgressBar.progressBackgroundTintList = totalTimeProgressBar.progressTintList
        totalTimeProgressBar.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(this, colorId))
    }

    private fun updateCurrentTimeColor(colorId: Int){
        var currentTimeProgressBar = findViewById<ProgressBar>(R.id.current_time_progress)
        currentTimeProgressBar.progress = 0
        currentTimeProgressBar.progressBackgroundTintList = currentTimeProgressBar.progressTintList
        currentTimeProgressBar.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(this, colorId))
    }

    private fun finishWorkout(){
        tts?.speak("Congrats! You've finished!", TextToSpeech.QUEUE_ADD, null, "")
        endWorkout()
    }

    private fun endWorkout(){
        writeHistory()

        val intent = Intent(this, FinishActivity::class.java).apply {
            putExtra(MESSAGE_COMPLETED, (timeSpentWorkingOut).toString())
        }
        startActivity(intent)
    }

    private fun writeHistory(){
        val historyFile = File(filesDir, historyFilename)
        if (!historyFile.exists())
            historyFile.createNewFile()

        val time = Calendar.getInstance().time
        val workout = title
        val duration = timeSpentWorkingOut

        historyFile.appendText("$time, $workout, $duration")
    }
}
