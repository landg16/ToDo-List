package ge.andghuladze.todoapp.fragments

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ge.andghuladze.todoapp.R
import ge.andghuladze.todoapp.adapters.NoteListRecyclerAdapter
import ge.andghuladze.todoapp.database.MyDB
import ge.andghuladze.todoapp.models.Note
import kotlinx.android.synthetic.main.note_list_fragment.*

class NoteListFragment : Fragment() {

    private lateinit var pinnedAdapter: NoteListRecyclerAdapter
    private lateinit var othersAdapter: NoteListRecyclerAdapter
    private lateinit var dataBase: MyDB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.note_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pinned_view.requestFocus() // for not showing keyboard on startup

        pinned_view.apply {
            layoutManager = StaggeredGridLayoutManager(2, 1)
            adapter = NoteListRecyclerAdapter()
            pinnedAdapter = adapter as NoteListRecyclerAdapter
        }

        others_view.apply {
            layoutManager = StaggeredGridLayoutManager(2, 1)
            adapter = NoteListRecyclerAdapter()
            othersAdapter = adapter as NoteListRecyclerAdapter
        }

        dataBase = activity?.applicationContext?.let { MyDB(it) }!!
        dataBase.loadDB()

        loadDatabaseIntoView()

        search_box.doAfterTextChanged {
            performSearch(it.toString())
        }

        take_a_note.setOnClickListener {
            val args = NoteListFragmentDirections.actionNoteListFragmentToNoteFragment(true, null)
            Navigation.findNavController(requireView()).navigate(args)
        }
    }

    private fun performSearch(str: String) {
        if (str.isEmpty() || str == "") {
            loadDatabaseIntoView()
            return
        }
        val pinnedList = dataBase.getSearchData(true, str)
        val othersList = dataBase.getSearchData(false, str)
        println("PINNED LIST LENGTH: " + pinnedList.size)
        println("OTHERS LIST LENGTH: " + othersList.size)
        setData(pinnedList, othersList, true)
    }

    private fun loadDatabaseIntoView() {
        var pinnedList = dataBase.getNoteData(true)
        var othersList = dataBase.getNoteData(false)

        if (pinnedList.size == 0) {
            pinnedList = dataBase.getNoteData(true)
        }

        if (othersList.size == 0) {
            othersList = dataBase.getNoteData(false)
        }

        setData(pinnedList, othersList, false)
    }

    private fun setData(
        pinnedList: MutableList<Note>,
        othersList: MutableList<Note>,
        isSearch: Boolean
    ) {
        val margins = pinned_container.layoutParams as ViewGroup.MarginLayoutParams

        if (pinnedList.size == 0 && !isSearch) {
            margins.topMargin = 0
            pinned_container.visibility = View.GONE
            others_text.visibility = View.GONE
        } else {
            margins.topMargin = 24.toDp(requireContext())
            pinned_container.visibility = View.VISIBLE
            others_text.visibility = View.VISIBLE
        }

        pinnedAdapter.addNoteList(listNotes = pinnedList)
        othersAdapter.addNoteList(listNotes = othersList)

        activity?.runOnUiThread {
            pinnedAdapter.notifyDataSetChanged()
            othersAdapter.notifyDataSetChanged()
        }
    }

    private fun Int.toDp(context: Context):Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,this.toFloat(),context.resources.displayMetrics
    ).toInt()
}