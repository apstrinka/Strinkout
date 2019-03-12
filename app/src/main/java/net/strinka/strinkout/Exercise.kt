package net.strinka.strinkout

class Exercise {
    val id: Int
    val name: String
    val sided: Boolean

    constructor(id: Int, name: String, sided: Boolean) {
        this.id = id
        this.name = name
        this.sided = sided
    }

    override fun toString(): String {
        return name
    }
}

val allExercises: Map<Int, Exercise> = mapOf(
    Pair(0, Exercise(0, "Abdominal Crunch", false)),
    Pair(1, Exercise(1, "Alternating Push-up Plank", false)),
    Pair(2, Exercise(2, "Back Leg Raise", false)),
    Pair(3, Exercise(3, "Bent Leg Twist", false)),
    Pair(4, Exercise(4, "Burpees", false)),
    Pair(5, Exercise(5, "Butt Kickers", false)),
    Pair(6, Exercise(6, "Calf Raises", false)),
    Pair(7, Exercise(7, "Chest Expander", false)),
    Pair(8, Exercise(8, "Diamond Push-ups", false)),
    Pair(9, Exercise(9, "Dive Bomber Push-ups", false)),
    Pair(10, Exercise(10, "Elevated Crunches", false)),
    Pair(11, Exercise(11, "Forward Lunges", false)),
    Pair(12, Exercise(12, "Frog Jumps", false)),
    Pair(13, Exercise(13, "Front Kicks", false)),
    Pair(14, Exercise(14, "Genie Sit", false)),
    Pair(15, Exercise(15, "High Jumper", false)),
    Pair(16, Exercise(16, "High Knees", false)),
    Pair(17, Exercise(17, "Hip Raise", false)),
    Pair(18, Exercise(18, "In and Out Abs", false)),
    Pair(19, Exercise(19, "Inch Worms", false)),
    Pair(20, Exercise(20, "Jump Rope Hops", false)),
    Pair(21, Exercise(21, "Jump Squats", false)),
    Pair(22, Exercise(22, "Jumping Jacks", false)),
    Pair(23, Exercise(23, "Jumping Planks", false)),
    Pair(24, Exercise(24, "Lateral Pillar Bridge", true)),
    Pair(25, Exercise(25, "Lateral Squats", false)),
    Pair(26, Exercise(26, "Leg Lifts", false)),
    Pair(27, Exercise(27, "Leg Spreaders", false)),
    Pair(28, Exercise(28, "Lying Tricep Lifts", false)),
    Pair(29, Exercise(29, "Mason Twist", false)),
    Pair(30, Exercise(30, "Mountain Climbers", false)),
    Pair(31, Exercise(31, "One Arm Side Push-up", true)),
    Pair(32, Exercise(32, "One Leg Circles", true)),
    Pair(33, Exercise(33, "Overhead Arm Clap", false)),
    Pair(34, Exercise(34, "Overhead Press", false)),
    Pair(35, Exercise(35, "Plank", false)),
    Pair(36, Exercise(36, "Plank with Arm Lift", false)),
    Pair(37, Exercise(37, "Pivoting Upper Cuts", false)),
    Pair(38, Exercise(38, "Power Circles", false)),
    Pair(39, Exercise(39, "Power Jump", false)),
    Pair(40, Exercise(40, "Push-up and Rotation", false)),
    Pair(41, Exercise(41, "Push-up on Knees", false)),
    Pair(42, Exercise(42, "Push-ups", false)),
    Pair(43, Exercise(43, "Quadruplex", false)),
    Pair(44, Exercise(44, "Reach Throughs", false)),
    Pair(45, Exercise(45, "Reach Ups", false)),
    Pair(46, Exercise(46, "Rear Lunges", false)),
    Pair(47, Exercise(47, "Reverse Plank", false)),
    Pair(48, Exercise(48, "Reverse V Lunges", false)),
    Pair(49, Exercise(49, "Running in Place", false)),
    Pair(50, Exercise(50, "Scissor Kicks", false)),
    Pair(51, Exercise(51, "Shoulder Tap Push-ups", false)),
    Pair(52, Exercise(52, "Side Bridge", true)),
    Pair(53, Exercise(53, "Side Circles", true)),
    Pair(54, Exercise(54, "Side Hops", false)),
    Pair(55, Exercise(55, "Side Leg Lifts", true)),
    Pair(56, Exercise(56, "Side Squats", false)),
    Pair(57, Exercise(57, "Side to Side Knee Lifts", false)),
    Pair(58, Exercise(58, "Single Leg Hops", true)),
    Pair(59, Exercise(59, "Single Leg Squats", true)),
    Pair(60, Exercise(60, "Sit-ups", false)),
    Pair(61, Exercise(61, "Six Inches and Hold", false)),
    Pair(62, Exercise(62, "Spiderman Push-up", false)),
    Pair(63, Exercise(63, "Sprinter", false)),
    Pair(64, Exercise(64, "Squat Jabs", false)),
    Pair(65, Exercise(65, "Squat Jacks", false)),
    Pair(66, Exercise(66, "Squat with Back Kick", false)),
    Pair(67, Exercise(67, "Squats", false)),
    Pair(68, Exercise(68, "Standing Side Crunch", false)),
    Pair(69, Exercise(69, "Steam Engine", false)),
    Pair(70, Exercise(70, "Step Touch", false)),
    Pair(71, Exercise(71, "Supermans", false)),
    Pair(72, Exercise(72, "Supine Bicycle", false)),
    Pair(73, Exercise(73, "Swimmer", false)),
    Pair(74, Exercise(74, "Switch Kick", false)),
    Pair(75, Exercise(75, "T Raise", false)),
    Pair(76, Exercise(76, "Teaser", false)),
    Pair(77, Exercise(77, "Tricep Dips", false)),
    Pair(78, Exercise(78, "Twisting Crunches", false)),
    Pair(79, Exercise(79, "Up Downs", false)),
    Pair(80, Exercise(80, "V Balance", false)),
    Pair(81, Exercise(81, "V Sit-ups", false)),
    Pair(82, Exercise(82, "Vertical Arm Rotations", false)),
    Pair(83, Exercise(83, "Wall Push-ups", false)),
    Pair(84, Exercise(84, "Wall Sit", false)),
    Pair(85, Exercise(85, "Wide Arm Push-ups", false)),
    Pair(86, Exercise(86, "Wide Squats", false)),
    Pair(87, Exercise(87, "Windmill", false))
)

