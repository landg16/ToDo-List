package ge.andghuladze.todoapp.database

import androidx.room.*
import ge.andghuladze.todoapp.models.NoteModel

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: NoteModel): Long

    @Update
    fun updateNote(note: NoteModel)

    @Delete
    fun deleteNote(note: NoteModel)

    @Query("SELECT DISTINCT(n.id), n.title, n.isPinned FROM Notes n LEFT JOIN EachNote en ON n.id = en.note_Id WHERE (n.title LIKE '%' || :str || '%' OR en.note LIKE '%' || :str || '%' ) AND n.isPinned = :isPinned")
    fun searchNote(str: String, isPinned: Boolean): List<NoteModel>

    @Query("SELECT * FROM Notes WHERE title == :name")
    fun getNoteByTitle(name: String): List<NoteModel>

    @Query("SELECT * FROM Notes")
    fun getNotes(): List<NoteModel>
}