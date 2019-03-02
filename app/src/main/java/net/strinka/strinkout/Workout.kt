package net.strinka.strinkout

class Workout {
    val name: String
    val exercises: List<Exercise>

    constructor(name: String, indices: List<Int>){
        this.name = name
        this.exercises = indices.map { i -> allExercises[i] }
    }
}

val defaultWorkouts = listOf(
    Workout("Standard", listOf(1,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,21,22,25,26,27,28,29,30,31,32,33,35,37,39,40,41,42,43,44,45,46,47,48,50,51,52,53,54,58,59,60,61,62,63,64,65,66,67,69,70,72,73,74,75,76,77,78,79)),
    Workout("Beginner", listOf(1,6,7,10,11,17,20,22,28,30,32,33,34,43,46,56,59,61,62,79)),
    Workout("Advanced", listOf(1,4,8,9,10,13,15,18,21,23,29,32,36,37,39,40,45,47,48,54,55,57,64,66,68,71,73))
)