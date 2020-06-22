package ge.andghuladze.todoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ge.andghuladze.todoapp.models.EachNote
import ge.andghuladze.todoapp.models.NoteModel

@Database(entities = [NoteModel::class, EachNote::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun eachNoteDao(): EachNoteDao

    companion object {
        var INSTANCE: AppDataBase? = null

        fun getAppDataBase(context: Context): AppDataBase? {
            if (INSTANCE == null) {
                synchronized(AppDataBase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java,
                        "keep_notes.db"
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }
}