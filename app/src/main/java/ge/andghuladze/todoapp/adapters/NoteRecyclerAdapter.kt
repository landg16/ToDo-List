package ge.andghuladze.todoapp.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import ge.andghuladze.todoapp.R
import ge.andghuladze.todoapp.listeners.OnCheckboxChanged
import ge.andghuladze.todoapp.listeners.OnEditTextChanged
import ge.andghuladze.todoapp.listeners.OnRemoveNoteClick
import ge.andghuladze.todoapp.models.EachNote
import kotlinx.android.synthetic.main.each_note_item.view.*


class NoteRecyclerAdapter(private val layoutId: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var noteList: MutableList<EachNote> = mutableListOf()
    private lateinit var context: Context
    private lateinit var onEditTextChanged: OnEditTextChanged
    private lateinit var onRemoveNoteClick: OnRemoveNoteClick
    private lateinit var onCheckboxChanged: OnCheckboxChanged

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
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
                holder.bind(noteList[position], context)

                holder.itemView.editText_item.doAfterTextChanged {
                    onEditTextChanged.onTextChanged(position, it.toString())
                }

                holder.itemView.delete_item.setOnClickListener {
                    onRemoveNoteClick.onRemoveClicked(position, noteList[position].isChecked)
                    it.setOnClickListener(null)
                }

                holder.itemView.checkbox_item.setOnCheckedChangeListener { btn, isChecked ->
                    onCheckboxChanged.onCheckboxClick(position, isChecked)
                    btn.setOnCheckedChangeListener(null)
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
        private var removeNote = viewItems.delete_item

        fun bind(note: EachNote, context: Context) {
            checkbox.isChecked = note.isChecked
            removeNote.visibility = View.GONE
            noteText.setText(note.note)

            if (note.isChecked) {
                val darkStateList = ContextCompat.getColorStateList(context, R.color.gray_tint)
                CompoundButtonCompat.setButtonTintList(checkbox, darkStateList)

                val editTextColor = ContextCompat.getColor(context, R.color.greyColor)
                noteText.paintFlags = noteText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                noteText.setTextColor(editTextColor)
            }

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