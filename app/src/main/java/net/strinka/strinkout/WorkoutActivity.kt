package net.strinka.strinkout

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Button
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
    private var currentExercise = ""
    private var nextExercise = ""
    private var exerciseList = mutableListOf("Reverse Plank", "Power Circles", "Push-up and Rotation", "Lying Tricep Lifts", "Spiderman Push-up", "T Raise",
        "Shoulder Tap Push-ups", "Chest Expander", "Dive Bomber Push-ups", "Jumping Jacks", "Wall Push-ups", "Alternating Push-up Plank", "Tricep Dips", "Wide Arm Push-ups",
        "Overhead Arm Clap", "Diamond Push-ups", "Overhead Press", "Push-ups", "Wall Sit", "Butt Kickers", "High Knees", "Genie Sit", "Calf Raises", "Frog Jumps",
        "Side to Side Knee Lifts", "High Jumper", "Hip Raise", "Reverse V Lunges", "Running in Place", "Front Kicks", "Mountain Climbers", "Rear Lunges",
        "Forward Lunges", "Jump Squats", "Squats", "Scissor Kicks", "Six Inches and Hold", "In and Out Abs", "Steam Engine", "Mason Twist", "Swimmer", "Quadruplex",
        "Bent Leg Twist", "Windmill", "Supermans", "Inch Worms", "Twisting Crunches", "Burpees", "Plank", "Supine Bicycle", "Leg Lifts", "Leg Spreaders",
        "Elevated Crunches", "V Sit-ups", "Sit-ups")
    //"One Arm Side Push-up", "Side Bridge", "Single Leg Squats", "Side Leg Lifts",

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

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

        val initListener = TextToSpeech.OnInitListener {
            fun onInit(status: Int) {
                if (status != TextToSpeech.ERROR) {
                    tts?.setLanguage(Locale.US);
                    tts?.setSpeechRate(0.6f)
                }
            }
        }
        tts = TextToSpeech(getApplicationContext(), initListener)
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        totalTimeLeft = message.toLong()*60000
        exerciseList.shuffle()
        findViewById<TextView>(R.id.total_time_remaing).text = millisToString(totalTimeLeft)
        exerciseIndex = 0
        exercisesSinceRest = 0
        currentActivityType = ActivityType.TRANSITION
        nextExercise = exerciseList[0]
        findViewById<TextView>(R.id.next_exercise).text = nextExercise
    }

    override fun onStop() {
        currentTimer?.pause()
        super.onStop()
    }

    fun start(view: View) {
        if (currentTimer == null) {
            findViewById<TextView>(R.id.next_exercise).text = exerciseList[0]
            tts?.speak("Welcome back.", TextToSpeech.QUEUE_ADD, null)
            startTransition()
        }
        currentTimer?.start()
        findViewById<Button>(R.id.start_button).isEnabled = false
        findViewById<Button>(R.id.pause_button).isEnabled = true
    }

    fun pause(view: View){
        currentTimer?.pause()
        findViewById<Button>(R.id.start_button).isEnabled = true
        findViewById<Button>(R.id.pause_button).isEnabled = false
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
                startNextExercise()
            }
        }
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
        tts?.speak(exerciseList[exerciseIndex], TextToSpeech.QUEUE_ADD, null)
        currentTimer = PausableTimer(30, currentActivityDuration, onTick, onFinish)
        currentTimer?.start()
    }

    private fun updateNextExercise(){
        exerciseIndex += 1
        if (exerciseIndex >= exerciseList.size){
            exerciseIndex = 0
            exerciseList.shuffle()
        }
        nextExercise = exerciseList[exerciseIndex]
    }

    private fun startNextExercise(){
        currentActivityType = ActivityType.EXERCISE
        currentActivityDuration =  Math.min(exerciseMillis, totalTimeLeft)
        currentExercise = nextExercise
        updateNextExercise()
        exercisesSinceRest += 1

        findViewById<TextView>(R.id.current_activity).text = currentExercise

        if (totalTimeLeft - currentActivityDuration <= 0){
            findViewById<TextView>(R.id.next_exercise).text = "Almost done!"
        } else if (exercisesSinceRest >= restAfter){
            findViewById<TextView>(R.id.next_exercise).text = "Rest"
        } else {
            findViewById<TextView>(R.id.next_exercise).text = nextExercise
        }


        tts?.speak("Begin", TextToSpeech.QUEUE_ADD, null)
        currentTimer = PausableTimer(30, currentActivityDuration, onTick, onFinish)
        if (exercisesSinceRest < restAfter && totalTimeLeft - currentActivityDuration > 0) {
            currentTimer?.addEvent(exerciseMillis - 10000, speak("Next exercise. $nextExercise"))
        }
        addCountdown(3)
        currentTimer?.start()
    }

    private fun startRest(){
        currentActivityType = ActivityType.REST
        currentActivityDuration = restMillis
        if (restsCount && totalTimeLeft < restMillis){
            currentActivityDuration = totalTimeLeft
        }
        findViewById<TextView>(R.id.current_activity).text = "Rest"
        findViewById<TextView>(R.id.next_exercise).text = exerciseList[exerciseIndex]
        exercisesSinceRest = 0
        tts?.speak("Rest for $restSeconds seconds", TextToSpeech.QUEUE_ADD, null)
        currentTimer = PausableTimer(30, currentActivityDuration, onTick, onFinish)
        if (totalTimeLeft - currentActivityDuration > 0) {
            currentTimer?.addEvent(restMillis - 10000, speak("Next exercise. $nextExercise"))
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
    }
}
