package ge.andghuladze.todoapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(
    var title: String,
    var eachNote: MutableList<EachNote>,
    var isPinned: Boolean = false
) : Parcelable