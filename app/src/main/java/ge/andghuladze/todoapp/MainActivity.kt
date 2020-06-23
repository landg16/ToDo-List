package ge.andghuladze.todoapp

import android.annotation.SuppressLint
import android.app.PendingIntent.getActivity
import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.view.Gravity
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ge.andghuladze.todoapp.database.MyDB
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var dataBase: MyDB

    private lateinit var takeANote: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dataBase = MyDB(this.applicationContext)

        dataBase.loadDB()
        initView()
        recyclerViewAdapter.addNoteList(listNotes = dataBase.getNoteData())
    }

    @SuppressLint("RtlHardcoded")
    private fun initView() {
        pinned_view.apply {
            layoutManager = StaggeredGridLayoutManager(2, 1)
            adapter = RecyclerViewAdapter()
            recyclerViewAdapter = adapter as RecyclerViewAdapter
        }

        takeANote = findViewById(R.id.take_a_note)

        takeANote.setOnClickListener {
            println("TAKE A NOTE CLICKEDDDD")
            val newNote = NoteFragment.newInstance()
            val slideTransition = Slide(Gravity.RIGHT)
            val exitTransition = Slide(Gravity.BOTTOM)
            exitTransition.duration = 300
            slideTransition.duration = 300
            val fade = Fade()
            fade.duration = 200
            newNote.allowEnterTransitionOverlap = true
            newNote.allowEnterTransitionOverlap = false
            newNote.enterTransition = slideTransition

            newNote.returnTransition = exitTransition

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.root_view, newNote, "carDetails")
                .addToBackStack(null)
                .commit()
        }
    }
}
