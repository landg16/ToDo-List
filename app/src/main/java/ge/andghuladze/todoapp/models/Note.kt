package ge.andghuladze.todoapp.models

data class Note(
    var title: String,
    var eachNote: List<EachNote>,
    var isPinned: Boolean = false
)