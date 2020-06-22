package ge.andghuladze.todoapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notes")
data class NoteModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    var title: String,
    var isPinned: Boolean = false
)