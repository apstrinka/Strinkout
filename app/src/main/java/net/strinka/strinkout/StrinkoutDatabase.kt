package net.strinka.strinkout

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.android.synthetic.main.activity_workout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Workout::class, WorkoutExercise::class, Exercise::class], version = 1)
abstract class StrinkoutDatabase : RoomDatabase() {
    abstract fun strinkoutDao(): StrinkoutDao

    companion object {
        @Volatile
        private var INSTANCE: StrinkoutDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): StrinkoutDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, StrinkoutDatabase::class.java, "Strinkout")
                    .addCallback(StrinkoutDatabaseCallback(scope)).build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class StrinkoutDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database -> scope.launch(Dispatchers.IO) {
                populateDatabase(database.strinkoutDao())
            } }
        }

        suspend fun populateDatabase(dao: StrinkoutDao){
            createExercises(dao)
            createWorkouts(dao)
        }

        suspend fun createExercises(dao: StrinkoutDao){
            dao.insert(Exercise(1, "Abdominal Crunch", false))
            dao.insert(Exercise(2, "Alternating Push-up Plank", false))
            dao.insert(Exercise(3, "Back Leg Raise", false))
            dao.insert(Exercise(4, "Bent Leg Twist", false))
            dao.insert(Exercise(5, "Burpees", false))
            dao.insert(Exercise(6, "Butt Kickers", false))
            dao.insert(Exercise(7, "Calf Raises", false))
            dao.insert(Exercise(8, "Chest Expander", false))
            dao.insert(Exercise(9, "Diamond Push-ups", false))
            dao.insert(Exercise(10, "Dive Bomber Push-ups", false))
            dao.insert(Exercise(11, "Elevated Crunches", false))
            dao.insert(Exercise(12, "Forward Lunges", false))
            dao.insert(Exercise(13, "Frog Jumps", false))
            dao.insert(Exercise(14, "Front Kicks", false))
            dao.insert(Exercise(15, "Genie Sit", false))
            dao.insert(Exercise(16, "High Jumper", false))
            dao.insert(Exercise(17, "High Knees", false))
            dao.insert(Exercise(18, "Hip Raise", false))
            dao.insert(Exercise(19, "In and Out Abs", false))
            dao.insert(Exercise(20, "Inch Worms", false))
            dao.insert(Exercise(21, "Jump Rope Hops", false))
            dao.insert(Exercise(22, "Jump Squats", false))
            dao.insert(Exercise(23, "Jumping Jacks", false))
            dao.insert(Exercise(24, "Jumping Planks", false))
            dao.insert(Exercise(25, "Lateral Pillar Bridge", true))
            dao.insert(Exercise(26, "Lateral Squats", false))
            dao.insert(Exercise(27, "Leg Lifts", false))
            dao.insert(Exercise(28, "Leg Spreaders", false))
            dao.insert(Exercise(29, "Lying Tricep Lifts", false))
            dao.insert(Exercise(30, "Mason Twist", false))
            dao.insert(Exercise(31, "Mountain Climbers", false))
            dao.insert(Exercise(32, "One Arm Side Push-up", true))
            dao.insert(Exercise(33, "One Leg Circles", true))
            dao.insert(Exercise(34, "Overhead Arm Clap", false))
            dao.insert(Exercise(35, "Overhead Press", false))
            dao.insert(Exercise(36, "Plank", false))
            dao.insert(Exercise(37, "Plank with Arm Lift", false))
            dao.insert(Exercise(38, "Pivoting Upper Cuts", false))
            dao.insert(Exercise(39, "Power Circles", false))
            dao.insert(Exercise(40, "Power Jump", false))
            dao.insert(Exercise(41, "Push-up and Rotation", false))
            dao.insert(Exercise(42, "Push-up on Knees", false))
            dao.insert(Exercise(43, "Push-ups", false))
            dao.insert(Exercise(44, "Quadruplex", false))
            dao.insert(Exercise(45, "Reach Throughs", false))
            dao.insert(Exercise(46, "Reach Ups", false))
            dao.insert(Exercise(47, "Rear Lunges", false))
            dao.insert(Exercise(48, "Reverse Plank", false))
            dao.insert(Exercise(49, "Reverse V Lunges", false))
            dao.insert(Exercise(50, "Running in Place", false))
            dao.insert(Exercise(51, "Scissor Kicks", false))
            dao.insert(Exercise(52, "Shoulder Tap Push-ups", false))
            dao.insert(Exercise(53, "Side Bridge", true))
            dao.insert(Exercise(54, "Side Circles", true))
            dao.insert(Exercise(55, "Side Hops", false))
            dao.insert(Exercise(56, "Side Leg Lifts", true))
            dao.insert(Exercise(57, "Side Squats", false))
            dao.insert(Exercise(58, "Side to Side Knee Lifts", false))
            dao.insert(Exercise(59, "Single Leg Hops", true))
            dao.insert(Exercise(60, "Single Leg Squats", true))
            dao.insert(Exercise(61, "Sit-ups", false))
            dao.insert(Exercise(62, "Six Inches and Hold", false))
            dao.insert(Exercise(63, "Spiderman Push-up", false))
            dao.insert(Exercise(64, "Sprinter", false))
            dao.insert(Exercise(65, "Squat Jabs", false))
            dao.insert(Exercise(66, "Squat Jacks", false))
            dao.insert(Exercise(67, "Squat with Back Kick", false))
            dao.insert(Exercise(68, "Squats", false))
            dao.insert(Exercise(69, "Standing Side Crunch", false))
            dao.insert(Exercise(70, "Steam Engine", false))
            dao.insert(Exercise(71, "Step Touch", false))
            dao.insert(Exercise(72, "Supermans", false))
            dao.insert(Exercise(73, "Supine Bicycle", false))
            dao.insert(Exercise(74, "Swimmer", false))
            dao.insert(Exercise(75, "Switch Kick", false))
            dao.insert(Exercise(76, "T Raise", false))
            dao.insert(Exercise(77, "Teaser", false))
            dao.insert(Exercise(78, "Tricep Dips", false))
            dao.insert(Exercise(79, "Twisting Crunches", false))
            dao.insert(Exercise(80, "Up Downs", false))
            dao.insert(Exercise(81, "V Balance", false))
            dao.insert(Exercise(82, "V Sit-ups", false))
            dao.insert(Exercise(83, "Vertical Arm Rotations", false))
            dao.insert(Exercise(84, "Wall Push-ups", false))
            dao.insert(Exercise(85, "Wall Sit", false))
            dao.insert(Exercise(86, "Wide Arm Push-ups", false))
            dao.insert(Exercise(87, "Wide Squats", false))
            dao.insert(Exercise(88, "Windmill", false))
            dao.insert(Exercise(89, "Ankle Taps", false))
            dao.insert(Exercise(90, "Archer Push-up", false))
            dao.insert(Exercise(91, "Back Arm Rowing", false))
            dao.insert(Exercise(92, "Back Bow Jacks", false))
            dao.insert(Exercise(93, "Back Leg Raise", false))
            dao.insert(Exercise(94, "Bent Knee Side Plank Hold", true))
            dao.insert(Exercise(95, "Biceps Curl", false))
            dao.insert(Exercise(96, "Break Dance", false))
            dao.insert(Exercise(97, "Chase the Rabbit", false))
            dao.insert(Exercise(98, "Cobra Wings", false))
            dao.insert(Exercise(99, "Crab Walk", false))
            dao.insert(Exercise(100, "Duck Walk", false))
            dao.insert(Exercise(101, "Fast Feet", false))
            dao.insert(Exercise(102, "Good Mornings", false))
            dao.insert(Exercise(103, "Hollow Body", false))
            dao.insert(Exercise(104, "In-Out Burpees", false))
            dao.insert(Exercise(105, "Iron Mike", false))
            dao.insert(Exercise(106, "Jump Rope High Knees", false))
            dao.insert(Exercise(107, "Knee In Push-ups", false))
            dao.insert(Exercise(108, "Knee to Chest Hops", false))
            dao.insert(Exercise(109, "Lateral Abductor Crunch", true))
            dao.insert(Exercise(110, "Lateral Abductor Crunch Beginner", true))
            dao.insert(Exercise(111, "Lateral High Knee March", false))
            dao.insert(Exercise(112, "Lunge Jumps", false))
            dao.insert(Exercise(113, "Lunge Plié", true))
            dao.insert(Exercise(114, "Pike Push-up", false))
            dao.insert(Exercise(115, "Plank Crunch and Kickback", true))
            dao.insert(Exercise(116, "Plank Hip Rotation", false))
            dao.insert(Exercise(117, "Plank Jacks", false))
            dao.insert(Exercise(118, "Plank Jump To Squat", false))
            dao.insert(Exercise(119, "Plank Side Walk", false))
            dao.insert(Exercise(120, "Plank Walk Out", false))
            dao.insert(Exercise(121, "Plank With Reach Back", false))
            dao.insert(Exercise(122, "Power Skip", false))
            dao.insert(Exercise(123, "Push-up and Clap", false))
            dao.insert(Exercise(124, "Push-up with Hip Extension", false))
            dao.insert(Exercise(125, "Quick Step Lunge and Jump", false))
            dao.insert(Exercise(126, "Reach and Pull", false))
            dao.insert(Exercise(127, "Side Shuffle", false))
            dao.insert(Exercise(128, "Side Sweep", false))
            dao.insert(Exercise(129, "Single Lateral Hops", false))
            dao.insert(Exercise(130, "Single Leg Calf Raise", true))
            dao.insert(Exercise(131, "Skaters", false))
            dao.insert(Exercise(132, "Skier Abs", false))
            dao.insert(Exercise(133, "Skipping For Height", false))
            dao.insert(Exercise(134, "Snow Angel", false))
            dao.insert(Exercise(135, "Standing Mountain Climber", false))
            dao.insert(Exercise(136, "Star Jumps", false))
            dao.insert(Exercise(137, "Super Skater Jump", false))
            dao.insert(Exercise(138, "Swipers", false))
            dao.insert(Exercise(139, "Switch Kick", false))
            dao.insert(Exercise(140, "The Hundred", false))
            dao.insert(Exercise(141, "Two-Point Plank", false))
            dao.insert(Exercise(142, "X Drill Alternate Feet", false))
            dao.insert(Exercise(143, "X Drill Rotations", false))
        }

        suspend fun createWorkouts(dao: StrinkoutDao){
            dao.createWorkout(1, "Beginner Full Body", false, listOf(2,7,8,11,12,18,21,23,30,32,33,34,36,37,38,42,47,50,59,65,68,70,71,83,88))
            dao.createWorkout(2, "Standard Full Body", false, listOf(2,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,22,23,25,27,28,29,30,31,32,34,35,36,37,39,41,43,44,45,46,47,48,49,50,51,52,53,56,57,58,60,61,62,63,67,68,69,70,72,73,74,76,78,79,81,82,84,85,86,87,88))
            dao.createWorkout(3, "Advanced Full Body", false, listOf(2,5,9,10,11,14,16,19,22,24,31,36,40,41,43,44,48,49,51,52,58,63,64,66,73,75,77,80,82))
            dao.createWorkout(4, "Quiet", false, listOf(2,4,7,8,9,10,11,12,14,15,18,19,20,25,27,28,29,30,31,32,34,35,36,37,39,41,43,44,45,46,47,48,49,51,52,53,56,58,60,61,62,63,67,68,69,70,72,73,74,76,78,79,81,82,84,85,86,87,88))
            dao.createWorkout(5, "Beginner Upper Body", false, listOf(95,8,98,23,29,32,34,35,38,39,42,126,48,65,76,83,85))
            dao.createWorkout(6, "Standard Upper Body", false, listOf(2,8,9,10,23,107,29,32,34,35,39,41,43,48,52,63,76,78,84,86))
            dao.createWorkout(7, "Advanced Upper Body", false, listOf(2,90,9,10,23,114,38,123,41,124,48,52,63,78,86))
            dao.createWorkout(8, "Beginner Core", false, listOf(1,11,15,102,18,110,30,36,119,44,46,53,62,134,69,70,72,140,88))
            dao.createWorkout(9, "Standard Core", false, listOf(61,82,11,28,27,73,36,5,79,20,72,1,89,91,4,103,19,109,30,116,121,44,46,51,53,62,70,74,81,88))
            dao.createWorkout(10, "Advanced Core", false, listOf(92,94,5,11,19,24,25,27,30,115,117,120,45,66,73,75,141,81))
            dao.createWorkout(11, "Beginner Lower Body", false, listOf(93,6,7,100,12,17,47,50,55,57,58,130,59,68,87))
            dao.createWorkout(12, "Standard Lower Body", false, listOf(6,7,100,12,13,14,15,16,17,18,22,113,31,47,49,50,56,58,60,68,85))
            dao.createWorkout(13, "Advanced Lower Body", false, listOf(99,101,13,105,22,112,31,125,51,56,60,66,67,80,87,142))
            dao.createWorkout(14, "Beginner Cardio", false, listOf(6,101,17,21,38,50,55,56,127,128,129,59,65,71))
            dao.createWorkout(15, "Standard Cardio", false, listOf(5,6,101,14,16,104,21,23,24,111,112,31,38,118,40,122,125,50,52,55,129,59,131,132,133,64,65,66,136,71,137,139,80,88))
            dao.createWorkout(16, "Advanced Cardio", false, listOf(96,5,97,99,104,106,22,24,108,38,117,122,125,127,131,135,137,138,143))
        }
    }
}

@Dao
interface StrinkoutDao {
    @Query("SELECT * FROM workout WHERE custom = 1 AND removed = 0 ORDER BY ordinal ASC")
    fun getAllCustomWorkouts(): LiveData<List<Workout>>

    @Query("SELECT * FROM exercise ORDER BY name")
    fun getAllExercises(): LiveData<List<Exercise>>

    @Query("SELECT * FROM workout WHERE workout_id = :workoutId")
    fun getWorkout(workoutId: Int): Workout?

    @Insert
    suspend fun insert(exercise: Exercise): Long

    @Insert
    suspend fun insert(workout: Workout): Long

    @Query("UPDATE workout SET ordinal = ordinal + :shift WHERE ordinal >= :minIndex AND ordinal <= :maxIndex")
    suspend fun reOrder(minIndex: Int, maxIndex: Int, shift: Int)

    @Update
    suspend fun update(workout: Workout)

    @Query("UPDATE workout SET name = :name WHERE workout_id = :workoutId")
    suspend fun updateWorkoutName(workoutId: Int, name: String)

    @Query("SELECT workout_id FROM workout WHERE rowid = :rowId")
    suspend fun getWorkoutId(rowId: Long): Int

    @Transaction
    suspend fun move(workout: Workout, toOrdinal: Int){
        val fromOrdinal = workout.ordinal
        if (fromOrdinal == toOrdinal)
            return
        if (fromOrdinal < toOrdinal)
            reOrder(fromOrdinal+1, toOrdinal, -1)
        else if (fromOrdinal > toOrdinal)
            reOrder(toOrdinal, fromOrdinal-1, 1)

        val newWorkout = Workout(workout.workoutId, workout.name, workout.custom, toOrdinal, workout.removed)
        update(newWorkout)
    }

    @Query("UPDATE workout SET removed = 1 WHERE workout_id = :workoutId")
    fun remove(workoutId: Int)

    @Query("SELECT MAX(ordinal) from workout")
    fun getMaxWorkoutOrdinal(): Int

    @Query("SELECT * from exercise AS e JOIN workout_exercise AS we ON e.exercise_id = we.exercise_id WHERE workout_id = :workoutId ORDER BY ordinal")
    fun getExercisesFromWorkout(workoutId: Int) : List<Exercise>

    @Insert
    suspend fun insert(workoutExercise: WorkoutExercise)

    @Query("SELECT MAX(ordinal) from workout_exercise WHERE workout_id = :workoutId")
    fun getMaxExerciseOrdinal(workoutId: Int): Int

    @Query("DELETE FROM workout_exercise WHERE workout_id = :workoutId")
    suspend fun removeExercisesFromWorkout(workoutId: Int)

    @Transaction
    suspend fun createWorkout(id: Int = 0, name: String, custom: Boolean, exerciseIds: List<Int>){
        val maxOrdinal = getMaxWorkoutOrdinal()
        val workout = Workout(0, name, custom, maxOrdinal + 1)
        val rowid = insert(workout)
        val workoutId = getWorkoutId(rowid)

        var index = 0
        for (id in exerciseIds){
            val workoutExercise = WorkoutExercise(0, workoutId, id, index)
            insert(workoutExercise)
            index += 1
        }
    }

    @Transaction
    suspend fun createWorkout(workout: Workout){
        val maxOrdinal = getMaxWorkoutOrdinal()
        val workout = workoutWithOrdinal(workout,maxOrdinal + 1)
        val rowid = insert(workout)
        val workoutId = getWorkoutId(rowid)

        var index = 0
        for (exercise in workout.exercises){
            val workoutExercise = WorkoutExercise(0, workoutId, exercise.exerciseId, index)
            insert(workoutExercise)
            index += 1
        }
    }

    @Transaction
    suspend fun updateWorkout(workoutId: Int, workout: Workout){
        updateWorkoutName(workoutId, workout.name)
        removeExercisesFromWorkout(workoutId)

        var index = 0
        for (exercise in workout.exercises){
            val workoutExercise = WorkoutExercise(0, workoutId, exercise.exerciseId, index)
            insert(workoutExercise)
            index += 1
        }
    }
}

class StrinkoutRepository(private val strinkoutDao: StrinkoutDao) {
    val allCustomWorkouts = strinkoutDao.getAllCustomWorkouts()

    val allExercises = strinkoutDao.getAllExercises()

    @WorkerThread
    suspend fun move(workout: Workout, toOrdinal: Int){
        strinkoutDao.move(workout, toOrdinal)
    }

    @WorkerThread
    fun remove(workoutId: Int){
        strinkoutDao.remove(workoutId)
    }

    @WorkerThread
    fun getExercises(workoutId: Int): List<Exercise> {
        return strinkoutDao.getExercisesFromWorkout(workoutId)
    }

    @WorkerThread
    fun getWorkout(workoutId: Int): Workout?{
        return strinkoutDao.getWorkout(workoutId)
    }

    @WorkerThread
    suspend fun createWorkout(workout: Workout){
        strinkoutDao.createWorkout(workout)
    }

    @WorkerThread
    suspend fun createWorkout(id: Int = 0, name: String, custom: Boolean, exerciseIds: List<Int>){
        strinkoutDao.createWorkout(id, name, custom, exerciseIds)
    }

    @WorkerThread
    suspend fun updateWorkout(workoutId: Int, workout: Workout){
        strinkoutDao.updateWorkout(workoutId, workout)
    }
}