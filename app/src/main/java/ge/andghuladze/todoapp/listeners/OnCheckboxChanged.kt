package ge.andghuladze.todoapp.listeners

interface OnCheckboxChanged {
    fun onCheckboxClick(position: Int, value:Boolean)
}