package ge.andghuladze.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import ge.andghuladze.todoapp.R
import ge.andghuladze.todoapp.adapters.NoteRecyclerViewAdapter
import ge.andghuladze.todoapp.models.EachNote
import ge.andghuladze.todoapp.models.Note
import kotlinx.android.synthetic.main.note_fragment.*

class NoteFragment : Fragment() {

    private lateinit var uncheckedAdapter: NoteRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,  savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.note_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        unchecked_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = NoteRecyclerViewAdapter()
            uncheckedAdapter = adapter as NoteRecyclerViewAdapter
        }

        val isNew = arguments?.getBoolean("isNew")
        var note: Note? = Note("", mutableListOf())
        if (isNew != null && !isNew) {
            note = arguments?.getParcelable("note")
        } else {
            note?.eachNote = mutableListOf()
            note?.eachNote?.add(EachNote(note_id = 0, note = "", isChecked = false))
        }

        if (note != null) {
            uncheckedAdapter.setNote(note)
        }

        add_note_item.setOnClickListener {
            note?.eachNote?.add(EachNote(note_id = 0, note = "", isChecked = false))
            if (note != null) {
                uncheckedAdapter.setNote(note)
            }
            uncheckedAdapter.notifyDataSetChanged()
        }

        back_btn.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_noteFragment_to_noteListFragment)
        }

        pin_btn.setOnClickListener {
            if (note != null) {
                note.isPinned = !note.isPinned
                if (note.isPinned) {
                    pin_btn.setImageResource(R.drawable.ic_pushpin_selected)
                } else {
                    pin_btn.setImageResource(R.drawable.ic_pushpin)
                }
            }
        }
    }
}