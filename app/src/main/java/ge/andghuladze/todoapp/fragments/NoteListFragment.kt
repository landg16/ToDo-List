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
import ge.andghuladze.todoapp.adapters.ListRecyclerViewAdapter
import ge.andghuladze.todoapp.database.MyDB
import kotlinx.android.synthetic.main.note_list_fragment.*

class NoteListFragment : Fragment() {

    private lateinit var pinnedAdapter: ListRecyclerViewAdapter
    private lateinit var othersAdapter: ListRecyclerViewAdapter
    private lateinit var dataBase: MyDB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.note_list_fragment, container, false)
    }

    @SuppressLint("RtlHardcoded")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        pinned_view.apply {
            layoutManager = StaggeredGridLayoutManager(2, 1)
            adapter = ListRecyclerViewAdapter()
            pinnedAdapter = adapter as ListRecyclerViewAdapter
        }

        others_view.apply {
            layoutManager = StaggeredGridLayoutManager(2, 1)
            adapter = ListRecyclerViewAdapter()
            othersAdapter = adapter as ListRecyclerViewAdapter
        }

        dataBase = activity?.applicationContext?.let { MyDB(it) }!!
        dataBase.loadDB()

        val pinnedList = dataBase.getNoteData(true)
        val othersList = dataBase.getNoteData(false)

        if (pinnedList.size == 0) {
            pinned_view.visibility = View.GONE
            pinned_text.visibility = View.GONE
            others_text.visibility = View.GONE
        } else {
            pinned_view.visibility = View.VISIBLE
            pinned_text.visibility = View.VISIBLE
            others_text.visibility = View.VISIBLE
        }

        pinnedAdapter.addNoteList(listNotes = pinnedList)
        othersAdapter.addNoteList(listNotes = othersList)

        take_a_note.setOnClickListener {
            val args = NoteListFragmentDirections.actionNoteListFragmentToNoteFragment(true, null)
            Navigation.findNavController(requireView()).navigate(args)
        }
    }
}