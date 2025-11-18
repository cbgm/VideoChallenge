package com.cbgm.videochallenge.util

import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import com.cbgm.persistence.dao.ExerciseDao
import com.cbgm.persistence.entity.ExerciseStatus
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UploadWorkerTest {

    private lateinit var context: Context
    private lateinit var exerciseDao: ExerciseDao

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        exerciseDao = mockk(relaxed = true)
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0
    }

    @Test
    fun `doWork should return success and update status when id is valid`() = runTest {
        val id = 123L
        val inputData = workDataOf("id" to id)
        val worker = TestListenableWorkerBuilder<UploadWorker>(context)
            .setWorkerFactory(FakeUploadWorkerFactory(exerciseDao))
            .setInputData(inputData)
            .build()

        val result = worker.doWork()

        coVerify { exerciseDao.updateStatus(id, ExerciseStatus.UPLOADING) }
        coVerify { exerciseDao.updateStatus(id, ExerciseStatus.UPLOADED) }
        assertEquals(ListenableWorker.Result.success(), result)
    }

    @Test
    fun `doWork should return failure when id is invalid`() = runTest {
        val inputData = workDataOf("id" to -1L)
        val worker = TestListenableWorkerBuilder<UploadWorker>(context)
            .setWorkerFactory(FakeUploadWorkerFactory(exerciseDao))
            .setInputData(inputData)
            .build()

        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.failure(), result)
    }
}

class FakeUploadWorkerFactory(private val exerciseDao: ExerciseDao) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return UploadWorker(appContext, workerParameters, exerciseDao)
    }
}