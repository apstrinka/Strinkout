package net.strinka.strinkout

class Exercise {
    val name: String
    val sided: Boolean

    constructor(name: String, sided: Boolean) {
        this.name = name
        this.sided = sided
    }

    override fun toString(): String {
        return name
    }
}

//Note: the index of this list is used an a unique identifier. Only add to the end, don't change the order.
val allExercises = listOf(
    Exercise("Abdominal Crunch", false),
    Exercise("Alternating Push-up Plank", false),
    Exercise("Back Leg Raise", false),
    Exercise("Bent Leg Twist", false),
    Exercise("Burpees", false),
    Exercise("Butt Kickers", false),
    Exercise("Calf Raises", false),
    Exercise("Chest Expander", false),
    Exercise("Diamond Push-ups", false),
    Exercise("Dive Bomber Push-ups", false),
    Exercise("Elevated Crunches", false),
    Exercise("Forward Lunges", false),
    Exercise("Frog Jumps", false),
    Exercise("Front Kicks", false),
    Exercise("Genie Sit", false),
    Exercise("High Jumper", false),
    Exercise("High Knees", false),
    Exercise("Hip Raise", false),
    Exercise("In and Out Abs", false),
    Exercise("Inch Worms", false),
    Exercise("Jump Rope Hops", false),
    Exercise("Jump Squats", false),
    Exercise("Jumping Jacks", false),
    Exercise("Jumping Planks", false),
    Exercise("Lateral Pillar Bridge", true),
    Exercise("Lateral Squats", false),
    Exercise("Leg Lifts", false),
    Exercise("Leg Spreaders", false),
    Exercise("Lying Tricep Lifts", false),
    Exercise("Mason Twist", false),
    Exercise("Mountain Climbers", false),
    Exercise("One Arm Side Push-up", true),
    Exercise("One Leg Circles", true),
    Exercise("Overhead Arm Clap", false),
    Exercise("Overhead Press", false),
    Exercise("Plank", false),
    Exercise("Plank with Arm Lift", false),
    Exercise("Pivoting Upper Cuts", false),
    Exercise("Power Circles", false),
    Exercise("Power Jump", false),
    Exercise("Push-up and Rotation", false),
    Exercise("Push-up on Knees", false),
    Exercise("Push-ups", false),
    Exercise("Quadruplex", false),
    Exercise("Reach Throughs", false),
    Exercise("Reach Ups", false),
    Exercise("Rear Lunges", false),
    Exercise("Reverse Plank", false),
    Exercise("Reverse V Lunges", false),
    Exercise("Running in Place", false),
    Exercise("Scissor Kicks", false),
    Exercise("Shoulder Tap Push-ups", false),
    Exercise("Side Bridge", true),
    Exercise("Side Circles", true),
    Exercise("Side Hops", false),
    Exercise("Side Leg Lifts", true),
    Exercise("Side Squats", false),
    Exercise("Side to Side Knee Lifts", false),
    Exercise("Single Leg Hops", true),
    Exercise("Single Leg Squats", true),
    Exercise("Sit-ups", false),
    Exercise("Six Inches and Hold", false),
    Exercise("Spiderman Push-up", false),
    Exercise("Sprinter", false),
    Exercise("Squat Jabs", false),
    Exercise("Squat Jacks", false),
    Exercise("Squat with Back Kick", false),
    Exercise("Squats", false),
    Exercise("Standing Side Crunch", false),
    Exercise("Steam Engine", false),
    Exercise("Step Touch", false),
    Exercise("Supermans", false),
    Exercise("Supine Bicycle", false),
    Exercise("Swimmer", false),
    Exercise("Switch Kick", false),
    Exercise("T Raise", false),
    Exercise("Teaser", false),
    Exercise("Tricep Dips", false),
    Exercise("Twisting Crunches", false),
    Exercise("Up Downs", false),
    Exercise("V Balance", false),
    Exercise("V Sit-ups", false),
    Exercise("Vertical Arm Rotations", false),
    Exercise("Wall Push-ups", false),
    Exercise("Wall Sit", false),
    Exercise("Wide Arm Push-ups", false),
    Exercise("Wide Squats", false),
    Exercise("Windmill", false)
)

