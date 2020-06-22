package ge.andghuladze.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ge.andghuladze.todoapp.database.MyDB
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var dataBase: MyDB


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("DAIBECHDE SHENI DEDA MOVTYAN")
        dataBase = MyDB(this.applicationContext)
        dataBase.loadDB()
        initView()
        Thread{
            dataBase.addData()
            recyclerViewAdapter.addNoteList(listNotes = dataBase.getNoteData())
        }.start()
    }

    private fun initView() {
        pinned_view.apply {
            layoutManager = StaggeredGridLayoutManager(2, 1)
            adapter = RecyclerViewAdapter()
            recyclerViewAdapter = adapter as RecyclerViewAdapter
        }
    }
}
