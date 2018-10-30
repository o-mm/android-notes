package com.example.ov_mm.notes.repository

import android.support.v4.util.Consumer
import com.example.ov_mm.notes.model.Note
import com.example.ov_mm.notes.service.dao.NotesDao
import com.example.ov_mm.notes.service.dao.NotesUpdateDao
import com.example.ov_mm.notes.vm.SyncInfo
import org.mockito.Mockito
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.Comparator

class NotesRepositoryMock(val atStart: CountDownLatch, val atFinish: CountDownLatch) : NotesRepository(
        Mockito.mock(NotesDao::class.java),
        Mockito.mock(NotesUpdateDao::class.java),
        NotesSyncTaskProvider {
            asyncConsumer ->
            object: NotesSyncTask {
                @Volatile var currentThread: Thread? = null
                @Volatile var cancelled: Boolean = false
                var lock: Lock = ReentrantLock()

                override fun run() {
                    lock.lock()
                    try {
                        if (cancelled)
                            return;
                        currentThread = Thread.currentThread()
                    } finally {
                        lock.unlock()
                    }
                    try {
                        atStart.await()
                        asyncConsumer.accept(SyncInfo.SyncResult.SUCCESS)
                    } finally {
                        atFinish.countDown()
                    }
                }

                override fun cancel() {
                    lock.lock()
                    try {
                        cancelled = true
                        currentThread?.interrupt() ?: atFinish.countDown()
                    } finally {
                        lock.unlock()
                    }
                }
            }
        }) {

    private val notes = (1..10).map { newNote(it) }

    override fun loadNotes(resultConsumer: Consumer<MutableList<NoteWrapper>>, query: String?, sortBy: SortProperty?, desc: Boolean): CancellableTask {
        resultConsumer.accept(
                notes.filter { n -> query == null || n.title?.contains(query) ?: false || n.content?.contains(query) ?: false }
                        .sortedWith(createComparator(sortBy, desc))
                        .toMutableList())
        return Mockito.mock(CancellableTask::class.java)
    }

    override fun saveNote(note: NoteWrapper) { /* do nothing */ }

    override fun deleteNote(note: NoteWrapper) { /* do nothing */ }

    override fun createNote(): NoteWrapper = NoteWrapper(Note())

    override fun getNote(id: Long): NoteWrapper? = null

    override fun getLastSyncDate(): Date? {
        val calendar = Calendar.getInstance()
        calendar.set(2000, 0, 1, 7, 15)
        return calendar.time
    }

    private fun newNote(i: Int): NoteWrapper {
        val note = Note();
        note.title = i.toString()
        note.content = i.toString()
        note.date = createDate(i)
        return NoteWrapper(note)
    }

    private fun createDate(i: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(1999, i, i, i, i)
        return calendar.time
    }

    private fun createComparator(sortBy: SortProperty?, desc: Boolean): Comparator<NoteWrapper?> {
        return Comparator { o1, o2 ->
            if (o1 === null || o2 === null) {
                throw NullPointerException("Null objects are not expected")
            }
            if (SortProperty.DATE == sortBy) {
                Objects.compare(o1.date, o2.date, createDefaultComparator()) * if (desc) -1 else 1
            } else if (SortProperty.TITLE == sortBy) {
                Objects.compare(o1.title, o2.title, createDefaultComparator()) * if (desc) -1 else 1
            } else {
                0
            }
        }
    }

    private fun <T> createDefaultComparator() : Comparator<T> {
        return Comparator<Comparable<T>> { o1, o2 ->
            if (o2 == null) {
                1
            } else if (o1 == null) {
                -1
            } else {
                o1.compareTo(o2 as T)
            }
        } as Comparator<T>
    }
}