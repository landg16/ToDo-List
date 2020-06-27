package ge.andghuladze.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import ge.andghuladze.todoapp.R
import ge.andghuladze.todoapp.adapters.NoteRecyclerViewAdapter
import ge.andghuladze.todoapp.database.MyDB
import ge.andghuladze.todoapp.listeners.OnCheckboxChanged
import ge.andghuladze.todoapp.listeners.OnEditTextChanged
import ge.andghuladze.todoapp.listeners.OnRemoveNoteClick
import ge.andghuladze.todoapp.models.EachNote
import ge.andghuladze.todoapp.models.Note
import ge.andghuladze.todoapp.models.NoteModel
import kotlinx.android.synthetic.main.note_fragment.*

class NoteFragment : Fragment(), OnEditTextChanged, OnRemoveNoteClick, OnCheckboxChanged {

    private lateinit var uncheckedAdapter: NoteRecyclerViewAdapter
    private var note: Note? = null
    private lateinit var myDB: MyDB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.note_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        myDB = activity?.applicationContext?.let { MyDB(it) }!!
        myDB.loadDB()

        unchecked_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = NoteRecyclerViewAdapter()
            uncheckedAdapter = adapter as NoteRecyclerViewAdapter
        }

        val isNew = arguments?.getBoolean("isNew")
        note = Note("", mutableListOf())
        if (isNew != null && !isNew) {
            note = arguments?.getParcelable("note")
        } else {
            note?.eachNote = mutableListOf()
            note?.eachNote?.add(EachNote(note_id = 0, note = "", isChecked = false))
        }

        if (note != null) {
            uncheckedAdapter.setNoteList(note!!.eachNote)
            uncheckedAdapter.setEditTextListener(this)
            uncheckedAdapter.setRemoveClickListener(this)
            uncheckedAdapter.setCheckboxListener(this)
        }

        add_note_item.setOnClickListener {
            note?.eachNote?.add(EachNote(note_id = 0, note = "", isChecked = false))
            if (note != null) {
                uncheckedAdapter.setNoteList(note!!.eachNote)
            }
            uncheckedAdapter.notifyDataSetChanged()
        }

        note_title.addTextChangedListener {
            note?.title = note_title.text.toString()
        }

        back_btn.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_noteFragment_to_noteListFragment)
            if (isNew != null && isNew) {
                if (note?.title != null) {
                    val noteModel = NoteModel(null, note!!.title, note!!.isPinned)
                    val noteList = note!!.eachNote
                    myDB.addNote(noteModel, noteList)
                }
            } else {
                if (note?.title != null) {
                    val noteModel = NoteModel(note!!.note_id, note!!.title, note!!.isPinned)
                    val noteList = note!!.eachNote
                    myDB.updateNote(noteModel)
                    if (noteModel.id != null) {
                        myDB.updateEachNote(noteModel.id, noteList)
                    }
                }
            }
        }

        pin_btn.setOnClickListener {
            if (note != null) {
                note!!.isPinned = !note!!.isPinned
                if (note?.isPinned!!) {
                    pin_btn.setImageResource(R.drawable.ic_pushpin_selected)
                } else {
                    pin_btn.setImageResource(R.drawable.ic_pushpin)
                }
            }
        }
    }

    override fun onTextChanged(position: Int, charSeq: String) {
        println("POSITION: $position, CHARSEQ: $charSeq")
        if(note != null && position < note?.eachNote?.size!!) {
            note?.eachNote?.get(position)?.note = charSeq
        }
    }

    override fun onClick(position: Int) {
        note?.eachNote?.removeAt(position)
        uncheckedAdapter.notifyDataSetChanged()
    }

    override fun onCheckboxClick(position: Int, value: Boolean) {
        println("POSITION: $position, BOOLEAN: $value")
        note?.eachNote?.get(position)?.isChecked = value
    }
}