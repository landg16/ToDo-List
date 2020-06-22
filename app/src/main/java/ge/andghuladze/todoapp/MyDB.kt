package ge.andghuladze.todoapp.database

import android.content.Context
import ge.andghuladze.todoapp.models.EachNote
import ge.andghuladze.todoapp.models.Note
import ge.andghuladze.todoapp.models.NoteModel

class MyDB(private var context: Context) {
    private var db: AppDataBase? = null
    private var noteDao: NoteDao? = null
    private var eachNoteDao: EachNoteDao? = null

    @Volatile
    private var isFinished: Boolean = false


    fun loadDB() {
        Thread {
            db = AppDataBase.getAppDataBase(context = context)
            noteDao = db?.noteDao()
            eachNoteDao = db?.eachNoteDao()
            isFinished = true
            println("FINISHED BLIAD")
        }.start()
    }

    fun addData(/*noteModel: NoteModel, eachNoteList: List<EachNote>*/) {
        while(!isFinished) {
            Thread.sleep(200)
        }
        val note1 = NoteModel(title = "testing", isPinned = false)
        val noteID = noteDao?.insertNote(note = note1)
        println("DEDA NAQACHI ID: $noteID")
        val eachNote1 = EachNote(note = "checkbox 1 is active", isChecked = true, note_id = noteID)
        val eachNote2 = EachNote(note = "checkbox 2 is inactive", isChecked = false, note_id = noteID)
        eachNoteDao?.insertEachNote(eachNote1)
        eachNoteDao?.insertEachNote(eachNote2)
    }

    fun getNoteData(): MutableList<Note> {
        val noteList : MutableList<Note> = mutableListOf()
        val noteModels: List<NoteModel>? = noteDao?.getNotes()

        if (noteModels != null) {
            print("SIZE OF NOTE MODELS: " + noteModels.size)
            for (eachNote in noteModels) {
                val eachList = eachNote.id?.let { eachNoteDao?.getEachById(note_id = it) }
                if (eachList != null) {
                    val note = Note(
                        title = eachNote.title,
                        eachNote = eachList,
                        isPinned = eachNote.isPinned
                    )
                    print("NOTE: $note")
                    noteList.add(note)
                } else {
                    val note = Note(
                        title = eachNote.title,
                        eachNote = arrayListOf(),
                        isPinned = eachNote.isPinned
                    )
                    println("NOTEE: $note")
                    noteList.add(note)
                }
            }
        } else {
            println("NOTE MODELS ARE NULL")
        }
        return noteList
    }
}