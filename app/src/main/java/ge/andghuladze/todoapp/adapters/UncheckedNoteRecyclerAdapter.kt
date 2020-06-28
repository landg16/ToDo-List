package ge.andghuladze.todoapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import ge.andghuladze.todoapp.R
import ge.andghuladze.todoapp.listeners.OnCheckboxChanged
import ge.andghuladze.todoapp.listeners.OnEditTextChanged
import ge.andghuladze.todoapp.listeners.OnRemoveNoteClick
import ge.andghuladze.todoapp.models.EachNote
import kotlinx.android.synthetic.main.each_note_item.view.*

class UncheckedNoteRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var noteList: MutableList<EachNote> = mutableListOf()
    private lateinit var onEditTextChanged: OnEditTextChanged
    private lateinit var onRemoveNoteClick: OnRemoveNoteClick
    private lateinit var onCheckboxChanged: OnCheckboxChanged

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UncheckedRecyclerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.each_note_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        println("ON BIND VIEW HOLDER FOR: $position")
        when (holder) {
            is UncheckedRecyclerViewHolder -> {
                holder.bind(noteList[position])
                holder.itemView.editText_item.doAfterTextChanged {
                    onEditTextChanged.onTextChanged(position, it.toString())
                }

                holder.itemView.delete_item.setOnClickListener {
                    onRemoveNoteClick.onRemoveClicked(position)
                }

                holder.itemView.checkbox_item.setOnCheckedChangeListener { _, isChecked ->
                    onCheckboxChanged.onCheckboxClick(position, isChecked)
                }
            }
        }
    }

    fun setNoteList(noteList: MutableList<EachNote>) {
        this.noteList = noteList
    }

    fun setEditTextListener(onEditTextChanged: OnEditTextChanged) {
        this.onEditTextChanged = onEditTextChanged
    }

    fun setRemoveClickListener(onRemoveNoteClick: OnRemoveNoteClick) {
        this.onRemoveNoteClick = onRemoveNoteClick
    }

    fun setCheckboxListener(onCheckboxChanged: OnCheckboxChanged) {
        this.onCheckboxChanged = onCheckboxChanged
    }

    class UncheckedRecyclerViewHolder constructor(viewItems: View) : RecyclerView.ViewHolder(viewItems) {
        private var checkbox = viewItems.checkbox_item
        private var noteText = viewItems.editText_item
        private var removeNote = viewItems.delete_item

        fun bind(note: EachNote) {
            checkbox.isChecked = note.isChecked
            noteText.setText(note.note)
            removeNote.visibility = View.GONE

            noteText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    removeNote.visibility = View.VISIBLE
                } else {
                    removeNote.visibility = View.GONE
                }
            }
        }

    }
}