package net.strinka.strinkout

class ExerciseList{
    private var exercises: MutableList<Exercise>
    private val shuffle: Boolean
    private var index: Int = 0
    var currentExercise: Exercise = Exercise(0, "Test", false)
    var nextExercise: Exercise

    constructor(){
        exercises = arrayListOf()
        shuffle = false
        nextExercise = currentExercise
    }

    constructor(workout: Workout, shuffle: Boolean){
        index = 0
        exercises = workout.exercises.toMutableList()
        this.shuffle = shuffle
        if (shuffle){
            exercises.shuffle()
        }
        nextExercise = exercises[0]
    }

    fun moveNext(){
        currentExercise = nextExercise
        index += 1
        if (index >= exercises.size){
            index = 0
            if (shuffle) {
                exercises.shuffle()
            }
        }
        nextExercise = exercises[index]
    }
}