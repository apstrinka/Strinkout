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
    Workout("Standard", listOf(1,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,21,22,24,26,27,28,29,30,31,33,34,35,36,38,40,42,43,44,45,46,47,48,49,50,51,52,55,56,57,59,60,61,62,66,67,68,69,71,72,73,75,77,78,80,81,83,84,85,86,87)),
    Workout("Beginner", listOf(1,6,7,10,11,17,20,22,29,31,32,33,35,36,37,41,46,49,58,64,67,69,70,82,87)),
    Workout("Advanced", listOf(1,4,8,9,10,13,15,18,21,23,30,35,39,40,42,43,47,48,50,51,57,62,63,65,72,74,76,79,81)),
    Workout("Standard w/o Jumps", listOf(1,3,6,7,8,9,10,11,13,14,17,18,19,24,26,27,28,29,30,31,33,34,35,36,38,40,42,43,44,45,46,47,48,50,51,52,55,57,59,60,61,62,66,67,68,69,71,72,73,75,77,78,80,81,83,84,85,86,87)),
    Workout("Test", listOf(1,2,3,31,52,55))
)