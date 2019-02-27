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
    private var exerciseSeconds: Long = 30
    private var transitionSeconds: Long = 5
    private var restSeconds: Long = 30
    private var restAfter = 5
    private var minutes: Long = 15
    private var currentTimer: PausableTimer? = null
    private var totalElapsed: Long = 0
    private var exerciseIndex = 0
    private var exercisesSinceRest = 0
    private var currentActivityType = ActivityType.TRANSITION
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
        exerciseSeconds = Math.max(preferences.getInt("exercise_seconds", 30).toLong(), 10)
        transitionSeconds = Math.max(preferences.getInt("transition_seconds", 5).toLong(), 1)
        restSeconds = Math.max(preferences.getInt("rest_seconds", 30).toLong(), 5)
        restAfter = Math.max(preferences.getInt("rest_after", 5), 1)

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
        minutes = message.toLong()
        exerciseList.shuffle()
        val placeholderText = findViewById<TextView>(R.id.total_time_remaing)
        placeholderText.text = minutes.toString() + ":00.00"
        exerciseIndex = 0
        exercisesSinceRest = 0
        currentActivityType = ActivityType.TRANSITION
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

    fun millisToString(millis: Long) : String{
        val min = millis / 60000
        val sec = (millis % 60000) / 1000
        val cen = (millis % 1000) / 10
        if (min > 0)
            return "$min:${sec.toString().padStart(2, '0')}.${cen.toString().padStart(2, '0')}"
        else
            return "$sec.${cen.toString().padStart(2, '0')}"
    }

    fun startNextActivity(){
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

    fun startTransition(){
        val onTick = fun(millisElapsed: Long){
            var millisLeft = transitionSeconds * 1000 - millisElapsed
            findViewById<TextView>(R.id.current_time_remaing).text = millisToString(millisLeft)
        }

        val onFinish = fun(){
            startNextActivity()
        }

        currentActivityType = ActivityType.TRANSITION
        findViewById<TextView>(R.id.current_activity).text = "Transition"
        tts?.speak(exerciseList[exerciseIndex], TextToSpeech.QUEUE_ADD, null)
        currentTimer = PausableTimer(30, transitionSeconds*1000, onTick, onFinish)
        currentTimer?.start()
    }

    fun startNextExercise(){
        val onTick = fun(millisElapsed: Long){
            var millisLeft = exerciseSeconds * 1000 - millisElapsed
            findViewById<TextView>(R.id.current_time_remaing).text = millisToString(millisLeft)
            var totalMillisLeft = minutes * 60000 - millisElapsed - totalElapsed
            findViewById<TextView>(R.id.total_time_remaing).text = millisToString(totalMillisLeft)
        }

        val onFinish = fun(){
            totalElapsed = totalElapsed + exerciseSeconds*1000
            findViewById<TextView>(R.id.total_time_remaing).text = millisToString(minutes*60000 - totalElapsed)
            if (totalElapsed < minutes*60000) {
                startNextActivity()
            } else {
                endWorkout()
            }
        }

        currentActivityType = ActivityType.EXERCISE
        findViewById<TextView>(R.id.current_activity).text = exerciseList[exerciseIndex]

        if (exerciseIndex >= exerciseList.size-2){
            exerciseIndex = -1
            exerciseList.shuffle()
        }

        if (totalElapsed + exerciseSeconds*1000 >= minutes*60000){
            findViewById<TextView>(R.id.next_exercise).text = "Almost done!"
        } else if (exercisesSinceRest + 1 >= restAfter){
            findViewById<TextView>(R.id.next_exercise).text = "Rest"
        } else {
            findViewById<TextView>(R.id.next_exercise).text = exerciseList[exerciseIndex + 1]
        }
        exerciseIndex += 1

        exercisesSinceRest += 1
        tts?.speak("Begin", TextToSpeech.QUEUE_ADD, null)
        currentTimer = PausableTimer(30, exerciseSeconds*1000, onTick, onFinish)
        if (exercisesSinceRest < restAfter && totalElapsed + exerciseSeconds*1000 < minutes*60000) {
            currentTimer?.addEvent((exerciseSeconds - 10) * 1000, {
                tts?.speak("Next exercise. " + exerciseList[exerciseIndex], TextToSpeech.QUEUE_ADD, null)
            })
        }
        currentTimer?.addEvent((exerciseSeconds-3)*1000, {tts?.speak("Three", TextToSpeech.QUEUE_ADD, null)})
        currentTimer?.addEvent((exerciseSeconds-2)*1000, {tts?.speak("Two", TextToSpeech.QUEUE_ADD, null)})
        currentTimer?.addEvent((exerciseSeconds-1)*1000, {tts?.speak("One", TextToSpeech.QUEUE_ADD, null)})
        currentTimer?.start()
    }

    fun startRest(){
        val onTick = fun(millisElapsed: Long){
            var millisLeft = restSeconds * 1000 - millisElapsed
            findViewById<TextView>(R.id.current_time_remaing).text = millisToString(millisLeft)
        }

        val onFinish = fun(){
            startNextActivity()
        }

        currentActivityType = ActivityType.REST
        findViewById<TextView>(R.id.current_activity).text = "Rest"
        findViewById<TextView>(R.id.next_exercise).text = exerciseList[exerciseIndex]
        exercisesSinceRest = 0
        tts?.speak("Rest for " + restSeconds + " seconds", TextToSpeech.QUEUE_ADD, null)
        currentTimer = PausableTimer(30, restSeconds*1000, onTick, onFinish)
        currentTimer?.addEvent((restSeconds-10)*1000, {
            tts?.speak("Next exercise. " + exerciseList[exerciseIndex], TextToSpeech.QUEUE_ADD, null)
        })
        currentTimer?.addEvent((restSeconds-3)*1000, {tts?.speak("Three", TextToSpeech.QUEUE_ADD, null)})
        currentTimer?.addEvent((restSeconds-2)*1000, {tts?.speak("Two", TextToSpeech.QUEUE_ADD, null)})
        currentTimer?.addEvent((restSeconds-1)*1000, {tts?.speak("One", TextToSpeech.QUEUE_ADD, null)})
        currentTimer?.start()
    }

    fun endWorkout(){
        tts?.speak("Congrats! You've finished!", TextToSpeech.QUEUE_ADD, null)
        findViewById<TextView>(R.id.current_time_remaing).text = millisToString(0)
        findViewById<TextView>(R.id.current_activity).text = "Congrats!"
        findViewById<TextView>(R.id.next_exercise).text = "You've Finished!"
        findViewById<Button>(R.id.start_button).isEnabled = false
        findViewById<Button>(R.id.pause_button).isEnabled = false
    }
}
