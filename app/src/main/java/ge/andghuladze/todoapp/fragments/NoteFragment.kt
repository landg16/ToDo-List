package ge.andghuladze.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ge.andghuladze.todoapp.MainActivity
import ge.andghuladze.todoapp.MyDB
import ge.andghuladze.todoapp.R
import ge.andghuladze.todoapp.adapters.NoteRecyclerAdapter
import ge.andghuladze.todoapp.listeners.OnCheckboxChanged
import ge.andghuladze.todoapp.listeners.OnEditTextChanged
import ge.andghuladze.todoapp.listeners.OnRemoveNoteClick
import ge.andghuladze.todoapp.models.EachNote
import ge.andghuladze.todoapp.models.Note
import ge.andghuladze.todoapp.models.NoteModel
import kotlinx.android.synthetic.main.note_fragment.*


class NoteFragment : Fragment(), OnEditTextChanged, OnRemoveNoteClick, OnCheckboxChanged {

    private lateinit var uncheckedAdapter: NoteRecyclerAdapter
    private lateinit var checkedAdapter: NoteRecyclerAdapter

    private val checkedList = mutableListOf<EachNote>()
    private val uncheckedList = mutableListOf<EachNote>()

    private var note: Note? = null
    private var isNew: Boolean = true
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

        val mainActivity = activity as MainActivity?
        if (mainActivity != null) {
            myDB = mainActivity.getDB()
        }

        applyAdapters()
        readNoteFromArgs()
        splitAdapterLists()
        addListeners()
    }

    private fun horizontalLineUpdate() {
        if (checkedList.isNotEmpty()) {
            horizontal_line.visibility = View.VISIBLE
            drop_down_image.visibility = View.VISIBLE
            checked_item.text = getString(R.string.checked_item, checkedList.size)
        } else {
            horizontal_line.visibility = View.GONE
            drop_down_image.visibility = View.GONE
        }
    }

    private fun splitAdapterLists() {
        for (each in note!!.eachNote) {
            if (each.isChecked) {
                checkedList.add(each)
            } else {
                uncheckedList.add(each)
            }
        }

        horizontalLineUpdate()

        setAdapter(checkedAdapter, checkedList)
        setAdapter(uncheckedAdapter, uncheckedList)
    }

    private fun setAdapter(adapter: NoteRecyclerAdapter, noteList: MutableList<EachNote>) {
        adapter.setNoteList(noteList)
        adapter.setEditTextListener(this)
        adapter.setRemoveClickListener(this)
        adapter.setCheckboxListener(this)
    }

    private fun addListeners() {
        add_note_item.setOnClickListener {
            uncheckedList.add(EachNote(note_id = 0, note = "", isChecked = false))
            uncheckedAdapter.notifyItemInserted(uncheckedList.size - 1)
        }

        note_title.addTextChangedListener {
            note?.title = note_title.text.toString()
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    println("BACK CLICKED")
                    saveChanges(isNew)
                    remove()
                    activity?.onBackPressed()
                }
            })

        back_btn.setOnClickListener {
            saveChanges(isNew)
            activity?.onBackPressed()
        }

        pin_btn.setOnClickListener {
            if (note != null) {
                note!!.isPinned = !note!!.isPinned
                checkNotePin()
            }
        }
    }

    private fun readNoteFromArgs() {
        isNew = arguments?.getBoolean("isNew")!!
        note = Note("", mutableListOf())

        if (!isNew) {
            note = arguments?.getParcelable("note")
            note_title.setText(note?.title)
        } else {
            note?.eachNote = mutableListOf()
            note?.eachNote?.add(EachNote(note_id = 0, note = "", isChecked = false))
        }
        checkNotePin()
    }

    private fun checkNotePin() {
        if (note?.isPinned!!) {
            pin_btn.setImageResource(R.drawable.ic_pushpin_selected)
        } else {
            pin_btn.setImageResource(R.drawable.ic_pushpin)
        }
    }

    private fun applyAdapters() {
        unchecked_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = NoteRecyclerAdapter(R.layout.each_note_item)
            uncheckedAdapter = adapter as NoteRecyclerAdapter
        }

        checked_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = NoteRecyclerAdapter(R.layout.each_note_item)
            checkedAdapter = adapter as NoteRecyclerAdapter
        }
    }

    private fun saveChanges(isNew: Boolean?) {
        note!!.eachNote.clear()
        note!!.eachNote.addAll(uncheckedList)
        note!!.eachNote.addAll(checkedList)

        if (note!!.title == "" || note!!.title.isEmpty()) {
            var isEmpty = true
            for (note in note!!.eachNote) {
                if (note.note.isNotEmpty() || note.isChecked) {
                    isEmpty = false
                    break
                }
            }
            if (isEmpty) {
                println("NOTE WAS EMPTY :(")
                return
            }
        }

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

    override fun onTextChanged(position: Int, charSeq: String) {
        if(position < uncheckedList.size) {
            uncheckedList[position].note = charSeq
        }
    }

    override fun onRemoveClicked(position: Int, isChecked: Boolean) {
        if (isChecked) {
            checkedList.removeAt(position)
            checkedAdapter.notifyItemRemoved(position)
        } else {
            uncheckedList.removeAt(position)
            uncheckedAdapter.notifyItemRemoved(position)
        }
    }

    override fun onCheckboxClick(position: Int, value: Boolean) {
        println("POSITION: $position, VALUE: $value")
        if (value) {
            println("UNCHECKED LIST: $uncheckedList")
            if(position < uncheckedList.size) {
                uncheckedList[position].isChecked = true
                checkedList.add(uncheckedList[position])
                uncheckedList.removeAt(position)

                unchecked_list.post {
                    uncheckedAdapter.notifyDataSetChanged()
                }
                checked_list.post {
                    checkedAdapter.notifyDataSetChanged()
                }
            }
        } else {
            println("CHECKED LIST: $checkedList")
            if(position < checkedList.size) {
                checkedList[position].isChecked = false
                uncheckedList.add(checkedList[position])
                checkedList.removeAt(position)

                unchecked_list.post {
                    uncheckedAdapter.notifyDataSetChanged()
                }
                checked_list.post {
                    checkedAdapter.notifyDataSetChanged()
                }
            }
        }
        horizontalLineUpdate()
    }
}