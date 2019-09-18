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
        }

        suspend fun createWorkouts(dao: StrinkoutDao){
            dao.createWorkout(1, "Standard", false, listOf(2,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,22,23,25,27,28,29,30,31,32,34,35,36,37,39,41,43,44,45,46,47,48,49,50,51,52,53,56,57,58,60,61,62,63,67,68,69,70,72,73,74,76,78,79,81,82,84,85,86,87,88))
            dao.createWorkout(2, "Beginner", false, listOf(2,7,8,11,12,18,21,23,30,32,33,34,36,37,38,42,47,50,59,65,68,70,71,83,88))
            dao.createWorkout(3, "Advanced", false, listOf(2,5,9,10,11,14,16,19,22,24,31,36,40,41,43,44,48,49,51,52,58,63,64,66,73,75,77,80,82))
            dao.createWorkout(4, "Quiet", false, listOf(2,4,7,8,9,10,11,12,14,15,18,19,20,25,27,28,29,30,31,32,34,35,36,37,39,41,43,44,45,46,47,48,49,51,52,53,56,58,60,61,62,63,67,68,69,70,72,73,74,76,78,79,81,82,84,85,86,87,88))
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