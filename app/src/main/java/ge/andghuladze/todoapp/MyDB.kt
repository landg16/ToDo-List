package ge.andghuladze.todoapp.database

import android.content.Context
import ge.andghuladze.todoapp.models.EachNote
import ge.andghuladze.todoapp.models.Note
import ge.andghuladze.todoapp.models.NoteModel
import java.util.concurrent.CountDownLatch
import java.util.concurrent.locks.ReentrantLock

class MyDB(private var context: Context) {
    private var db: AppDataBase? = null
    private var noteDao: NoteDao? = null
    private var eachNoteDao: EachNoteDao? = null
    private var waiter: ReentrantLock = ReentrantLock()

    @Synchronized
    fun loadDB() {
        Thread {
            waiter.lock()
            db = AppDataBase.getAppDataBase(context = context)
            noteDao = db?.noteDao()
            eachNoteDao = db?.eachNoteDao()
            waiter.unlock()
        }.start()
    }

    fun addData(noteModel: NoteModel, eachNoteList: List<EachNote>) {
        Thread {
            waiter.lock()
//            val note1 = NoteModel(title = "testing", isPinned = false)
            val noteID = noteDao?.insertNote(note = noteModel)
//            val eachNote1 =
//                EachNote(note = "checkbox 1 is active", isChecked = true, note_id = noteID)
//            val eachNote2 =
//                EachNote(note = "checkbox 2 is inactive", isChecked = false, note_id = noteID)

            for (note in eachNoteList) {
                note.note_id = noteID
                eachNoteDao?.insertEachNote(note)
            }
            waiter.unlock()
        }.start()
    }

    fun deleteEachNote(eachNote: EachNote) {
        Thread {
            waiter.lock()
            eachNoteDao?.deleteNote(eachNote)
            waiter.unlock()
        }.start()
    }

    fun getNoteData(isPinned: Boolean): MutableList<Note> {
        val countDownLatch = CountDownLatch(1)
        val noteList: MutableList<Note> = mutableListOf()
        Thread {
            waiter.lock()
            val noteModels: List<NoteModel>? = noteDao?.getNotes()

            if (noteModels != null) {
                for (eachNote in noteModels) {
                    if (eachNote.isPinned == isPinned) {
                        val eachList = eachNote.id?.let { eachNoteDao?.getEachById(note_id = it) }
                        if (eachList != null) {
                            val note = Note(
                                title = eachNote.title,
                                eachNote = eachList,
                                isPinned = eachNote.isPinned
                            )
                            noteList.add(note)
                        } else {
                            val note = Note(
                                title = eachNote.title,
                                eachNote = arrayListOf(),
                                isPinned = eachNote.isPinned
                            )
                            noteList.add(note)
                        }
                    }
                }
            }
            countDownLatch.countDown()
            waiter.unlock()
        }.start()
        countDownLatch.await()
        return noteList
    }
}