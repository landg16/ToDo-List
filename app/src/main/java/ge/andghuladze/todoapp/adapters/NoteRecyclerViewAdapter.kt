package ge.andghuladze.todoapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.andghuladze.todoapp.R
import ge.andghuladze.todoapp.models.EachNote
import ge.andghuladze.todoapp.models.Note
import kotlinx.android.synthetic.main.each_note_item.view.*

class NoteRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var note: Note
    private var noteList: MutableList<EachNote> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NoteRecycleHolder(
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
        when (holder) {
            is NoteRecycleHolder -> {
                holder.bind(noteList[position])
            }
        }
    }

    fun setNote(note: Note) {
        this.note = note
        this.noteList = note.eachNote
        println("SET NOTE CALLED")
    }

    class NoteRecycleHolder constructor(viewItems: View) : RecyclerView.ViewHolder(viewItems) {
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

            removeNote.setOnClickListener {
                println("REMOVE CALLED")
            }
        }

    }
}