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

    @Query("SELECT * FROM EachNote WHERE note_id == :note_id")
    fun getEachById(note_id: Long): List<EachNote>

    @Query("SELECT * FROM EachNote")
    fun getNotes(): List<EachNote>
}