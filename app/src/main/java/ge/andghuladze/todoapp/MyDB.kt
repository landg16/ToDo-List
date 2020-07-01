package ge.andghuladze.todoapp

import android.content.Context
import ge.andghuladze.todoapp.database.AppDataBase
import ge.andghuladze.todoapp.database.EachNoteDao
import ge.andghuladze.todoapp.database.NoteDao
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

    fun addNote(noteModel: NoteModel, eachNoteList: List<EachNote>) {
        Thread {
            waiter.lock()
            val noteID = noteDao?.insertNote(note = noteModel)
            for (note in eachNoteList) {
                note.note_id = noteID
                eachNoteDao?.insertEachNote(note)
            }
            waiter.unlock()
        }.start()
    }

    fun updateNote(noteModel: NoteModel) {
        Thread {
            waiter.lock()
            noteDao?.updateNote(noteModel)
            waiter.unlock()
        }.start()
    }

    fun updateEachNote(note_id: Long, eachNoteList: List<EachNote>) {
        Thread {
            waiter.lock()
            eachNoteDao?.deleteById(note_id)
            for(eachNote in eachNoteList) {
                println(eachNote.note)
                eachNote.note_id = note_id
                eachNoteDao?.insertEachNote(eachNote)
            }
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
                        var eachList = eachNote.id?.let { eachNoteDao?.getEachById(note_id = it) }

                        if (eachList == null) {
                            eachList = arrayListOf()
                        }

                        val note = Note(
                            title = eachNote.title,
                            eachNote = eachList,
                            isPinned = eachNote.isPinned,
                            note_id = eachNote.id
                        )
                        noteList.add(note)
                    }
                }
            }
            countDownLatch.countDown()
            waiter.unlock()
        }.start()
        countDownLatch.await()
        return noteList
    }

    fun getSearchData(isPinned: Boolean, str: String): MutableList<Note> {
        println("SEARCH STARTED: $str")
        val countDownLatch = CountDownLatch(1)
        val noteList: MutableList<Note> = mutableListOf()
        Thread {
            waiter.lock()
            val noteModels: List<NoteModel>? = noteDao?.searchNote(str, isPinned)
            if (noteModels != null) {
                println("MODELS LENGTH: " + noteModels.size)
                for (eachNote in noteModels) {
                    println("NOTE: " + eachNote.id)
                    var eachList = eachNote.id?.let { eachNoteDao?.getEachById(note_id = it) }
                    println("EACH LIST LENGTH: " + eachList?.size)
                    if (eachList == null) {
                        eachList = arrayListOf()
                    }

                    val note = Note(
                        title = eachNote.title,
                        eachNote = eachList,
                        isPinned = eachNote.isPinned,
                        note_id = eachNote.id
                    )
                    noteList.add(note)
                }
            }
            countDownLatch.countDown()
            waiter.unlock()
        }.start()
        countDownLatch.await()
        return noteList
    }
}