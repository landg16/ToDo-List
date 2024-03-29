package ge.andghuladze.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var myDB: MyDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myDB = MyDB(this)
        myDB.loadDB()
    }

    fun getDB(): MyDB {
        return myDB
    }
}
