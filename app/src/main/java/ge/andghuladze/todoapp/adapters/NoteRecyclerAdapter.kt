package ge.andghuladze.todoapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import ge.andghuladze.todoapp.listeners.OnCheckboxChanged
import ge.andghuladze.todoapp.listeners.OnEditTextChanged
import ge.andghuladze.todoapp.listeners.OnRemoveNoteClick
import ge.andghuladze.todoapp.models.EachNote
import kotlinx.android.synthetic.main.each_crossed_note_item.view.*
import kotlinx.android.synthetic.main.each_note_item.view.*
import kotlinx.android.synthetic.main.each_note_item.view.checkbox_item
import kotlinx.android.synthetic.main.each_note_item.view.delete_item

class NoteRecyclerAdapter(private val layoutId: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var noteList: MutableList<EachNote> = mutableListOf()
    private lateinit var onEditTextChanged: OnEditTextChanged
    private lateinit var onRemoveNoteClick: OnRemoveNoteClick
    private lateinit var onCheckboxChanged: OnCheckboxChanged

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UncheckedRecyclerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                layoutId,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UncheckedRecyclerViewHolder -> {
                holder.bind(noteList[position])

                if (!noteList[position].isChecked) {
                    holder.itemView.editText_item.doAfterTextChanged {
                        onEditTextChanged.onTextChanged(position, it.toString())
                    }
                }

                holder.itemView.delete_item.setOnClickListener {
                    onRemoveNoteClick.onRemoveClicked(position, noteList[position].isChecked)
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

    class UncheckedRecyclerViewHolder constructor(viewItems: View) :
        RecyclerView.ViewHolder(viewItems) {
        private var checkbox = viewItems.checkbox_item
        private var noteText = viewItems.editText_item
        private var noteTextView = viewItems.textView_item
        private var removeNote = viewItems.delete_item

        fun bind(note: EachNote) {
            checkbox.isChecked = note.isChecked
            removeNote.visibility = View.GONE

            if (note.isChecked) {
                noteTextView.text = note.note
            } else {
                noteText.setText(note.note)
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
}