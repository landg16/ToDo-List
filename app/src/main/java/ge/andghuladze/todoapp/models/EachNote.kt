package ge.andghuladze.todoapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "EachNote")
data class EachNote(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var note: String,
    var isChecked: Boolean,
    var note_id: Long?
)