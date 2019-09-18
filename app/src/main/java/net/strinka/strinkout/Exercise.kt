package net.strinka.strinkout

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise")
class Exercise(
    @PrimaryKey @ColumnInfo(name = "exercise_id")val exerciseId: Int,
    @NonNull val name: String,
    @NonNull val sided: Boolean
){

    override fun toString(): String {
        return name
    }
}

