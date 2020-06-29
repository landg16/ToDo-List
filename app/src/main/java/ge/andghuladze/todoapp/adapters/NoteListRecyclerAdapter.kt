package ge.andghuladze.todoapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import ge.andghuladze.todoapp.R
import ge.andghuladze.todoapp.fragments.NoteListFragmentDirections
import ge.andghuladze.todoapp.models.Note
import kotlinx.android.synthetic.main.note_list_item.view.*


class NoteListRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<Note> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecycleViewerHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.note_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RecycleViewerHolder -> {
                holder.bind(list[position])
                holder.itemView.setOnClickListener{
                    val args = NoteListFragmentDirections.actionNoteListFragmentToNoteFragment(false, list[position])
                    Navigation.findNavController(it).navigate(args)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addNoteList(listNotes: List<Note>) {
        list = listNotes
    }

    class RecycleViewerHolder constructor(viewItems: View) : RecyclerView.ViewHolder(viewItems) {

        private val title = viewItems.title
        private val firstCheckBox = viewItems.first_check_box
        private val secondCheckBox = viewItems.second_check_box
        private val plusText = viewItems.plus_text

        fun bind(note: Note) {
            title.text = note.title
            val listSize = note.eachNote.size

            if (listSize > 0) {
                firstCheckBox.text = note.eachNote[0].note
                firstCheckBox.isChecked = note.eachNote[0].isChecked
            } else {
                firstCheckBox.visibility = View.GONE
            }

            if (listSize > 1) {
                secondCheckBox.text = note.eachNote[1].note
                secondCheckBox.isChecked = note.eachNote[1].isChecked
            } else {
                secondCheckBox.visibility = View.GONE
            }

            if (listSize > 2) {
                val str = "+" + (listSize - 2) + " checked item"
                plusText.text = str
            } else {
                plusText.visibility = View.GONE
            }
        }

    }

}