package ge.andghuladze.todoapp.listeners

interface OnEditTextChanged {
    fun onTextChanged(position: Int, charSeq: String)
}