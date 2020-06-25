package ge.andghuladze.todoapp.database

import androidx.room.*
import ge.andghuladze.todoapp.models.EachNote

@Dao
interface EachNoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEachNote(note: EachNote)

    @Update
    fun updateEachNote(note: EachNote)

    @Delete
    fun deleteNote(note: EachNote)

    @Query("DELETE FROM EachNote WHERE note_id == :note_id")
    fun deleteById(note_id: Long)

    @Query("SELECT * FROM EachNote WHERE note_id == :note_id")
    fun getEachById(note_id: Long): MutableList<EachNote>

    @Query("SELECT * FROM EachNote")
    fun getNotes(): List<EachNote>
}