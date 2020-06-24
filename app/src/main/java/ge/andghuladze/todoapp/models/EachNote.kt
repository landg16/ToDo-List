package ge.andghuladze.todoapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "EachNote")
@Parcelize
data class EachNote(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var note: String,
    var isChecked: Boolean,
    var note_id: Long?
) : Parcelable