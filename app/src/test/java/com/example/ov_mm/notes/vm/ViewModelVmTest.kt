package com.example.ov_mm.notes.vm

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ov_mm.notes.repository.NoteWrapper
import com.example.ov_mm.notes.repository.NotesRepositoryMock
import com.example.ov_mm.notes.repository.SortProperty
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch

class ViewModelVmTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun sortingTest() {
        var vm = createViewNotesVm()
        assertNotNull(vm.lastSyncText)
        assertEquals("2000-01-01 07:15", vm.lastSyncText.value)

        vm.init("1", SortProperty.DATE, true)
        var notes: MutableList<NoteWrapper> = vm.notes.value ?: throw AssertionError()
        assertEquals(2, notes.size)
        assertFalse(notes.get(0).date?.before(notes.get(1).date) ?: true)

        vm = createViewNotesVm()
        vm.init("1", SortProperty.DATE, false)
        notes = vm.notes.value ?: throw AssertionError()
        assertEquals(2, notes.size)
        assertFalse(notes.get(1).date?.before(notes.get(0).date) ?: true)

        vm = createViewNotesVm()
        vm.init("1", SortProperty.TITLE, true)
        notes = vm.notes.value ?: throw AssertionError()
        assertEquals(2, notes.size)
        assertEquals(1, notes.get(0).title?.compareTo(notes.get(1).title!!))

        vm = createViewNotesVm()
        vm.init("1", SortProperty.TITLE, false)
        notes = vm.notes.value ?: throw AssertionError()
        assertEquals(2, notes.size)
        assertEquals(-1, notes.get(0).title?.compareTo(notes.get(1).title!!))

        vm.onOrderChanged(true)
        notes = vm.notes.value ?: throw AssertionError()
        assertEquals(2, notes.size)
        assertEquals(1, notes.get(0).title?.compareTo(notes.get(1).title!!))

        vm.onSortPropertyChanged(SortProperty.DATE)
        notes = vm.notes.value ?: throw AssertionError()
        assertEquals(2, notes.size)
        assertFalse(notes.get(0).date?.before(notes.get(1).date) ?: true)

        vm.onSearch(null)
        notes = vm.notes.value ?: throw AssertionError()
        assertEquals(10, notes.size)
        notes.windowed(2).forEach({ list -> assertFalse(list.get(0).date?.before(list.get(1).date) ?: true) })
    }

    @Test(timeout = 10000)
    fun syncTaskTest() {
        val atStart = CountDownLatch(1)
        val atFinish = CountDownLatch(1)
        val vm = createViewNotesVm(atStart, atFinish)
        assertNull(vm.syncInfo.running.value)
        assertNull(vm.syncInfo.syncResult.value)
        vm.startSyncTask()
        assertTrue(vm.syncInfo.running.value ?: false)
        assertNull(vm.syncInfo.syncResult.value)
        atStart.countDown()
        atFinish.await()
        assertEquals(SyncInfo.SyncResult.SUCCESS, vm.syncInfo.syncResult.value)
        assertEquals(false, vm.syncInfo.running.value)
    }

    @Test(timeout = 10000)
    fun cancelSyncTaskTest() {
        val atFinish = CountDownLatch(1)
        val vm = createViewNotesVm(atFinish = atFinish)
        assertNull(vm.syncInfo.running.value)
        assertNull(vm.syncInfo.syncResult.value)
        vm.startSyncTask()
        assertTrue(vm.syncInfo.running.value ?: false)
        assertNull(vm.syncInfo.syncResult.value)
        vm.cancelSyncTask()
        atFinish.await()
        assertNull(vm.syncInfo.syncResult.value)
        assertEquals(false, vm.syncInfo.running.value)
    }

    private fun createViewNotesVm(atStart: CountDownLatch = CountDownLatch(1),
                                  atFinish: CountDownLatch = CountDownLatch(1)): ViewNotesVm {
        return ViewNotesVm(NotesRepositoryMock(atStart, atFinish))
    }
}