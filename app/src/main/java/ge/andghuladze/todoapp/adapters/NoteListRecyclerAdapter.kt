package ge.andghuladze.todoapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import ge.andghuladze.todoapp.R
import ge.andghuladze.todoapp.fragments.NoteListFragmentDirections
import ge.andghuladze.todoapp.models.EachNote
import ge.andghuladze.todoapp.models.Note
import kotlinx.android.synthetic.main.note_list_item.view.*


class NoteListRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var context: Context
    private var list: List<Note> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
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
                holder.bind(list[position], context)
                holder.itemView.setOnTouchListener { view, motionEvent ->
                    when (motionEvent.actionMasked) {
                        MotionEvent.ACTION_DOWN -> {
                            val args =
                                NoteListFragmentDirections.actionNoteListFragmentToNoteFragment(
                                    false,
                                    list[position]
                                )
                            Navigation.findNavController(view).navigate(args)
                            true
                        }

                        MotionEvent.ACTION_UP -> {
                            view.performClick()
                            true
                        }

                        else -> {
                            false
                        }
                    }
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
        private val checkboxList = viewItems.checkbox_list
        private val plusText = viewItems.plus_text

        fun bind(note: Note, context: Context) {

            if (note.title.isEmpty()) {
                title.visibility = View.GONE
            } else {
                title.text = note.title
            }

            val checkedList = mutableListOf<EachNote>()

            checkboxList.removeAllViewsInLayout()

            for (nt in note.eachNote) {
                if (nt.isChecked) {
                    checkedList.add(nt)
                } else {
                    val checkbox = CheckBox(context)

                    checkbox.text = nt.note
                    checkbox.isChecked = false
                    checkbox.isEnabled = false
                    checkbox.isFocusable = false
                    checkbox.isClickable = false

                    checkboxList.addView(checkbox)
                }
            }

            if (checkedList.size > 0) {
                val str = "+" + checkedList.size + " checked item"
                plusText.text = str
            } else {
                plusText.visibility = View.GONE
            }
        }

    }

}