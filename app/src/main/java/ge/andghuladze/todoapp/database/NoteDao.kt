package ge.andghuladze.todoapp.database

import androidx.room.*
import ge.andghuladze.todoapp.models.NoteModel

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: NoteModel) : Long

    @Update
    fun updateNote(note: NoteModel)

    @Delete
    fun deleteNote(note: NoteModel)

    @Query("SELECT * FROM Notes WHERE title == :name")
    fun getNoteByTitle(name: String): List<NoteModel>

    @Query("SELECT * FROM Notes")
    fun getNotes(): List<NoteModel>
}