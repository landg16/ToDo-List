package ge.andghuladze.todoapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ge.andghuladze.todoapp.R
import ge.andghuladze.todoapp.RecyclerViewAdapter
import ge.andghuladze.todoapp.database.MyDB
import kotlinx.android.synthetic.main.note_list_fragment.*

class NoteListFragment : Fragment() {

    private lateinit var pinnedAdapter: RecyclerViewAdapter
    private lateinit var othersAdapter: RecyclerViewAdapter
    private lateinit var dataBase: MyDB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.note_list_fragment, container, false)
    }

    @SuppressLint("RtlHardcoded")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        pinned_view.apply {
            layoutManager = StaggeredGridLayoutManager(2, 1)
            adapter = RecyclerViewAdapter()
            pinnedAdapter = adapter as RecyclerViewAdapter
        }

        others_view.apply {
            layoutManager = StaggeredGridLayoutManager(2, 1)
            adapter = RecyclerViewAdapter()
            othersAdapter = adapter as RecyclerViewAdapter
        }

        dataBase = activity?.applicationContext?.let { MyDB(it) }!!
        dataBase.loadDB()

        pinnedAdapter.addNoteList(listNotes = dataBase.getNoteData(true))
        othersAdapter.addNoteList(listNotes = dataBase.getNoteData(false))

        take_a_note.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_noteListFragment_to_noteFragment)
        }
    }
}