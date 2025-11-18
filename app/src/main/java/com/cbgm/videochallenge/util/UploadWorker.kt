package com.cbgm.videochallenge.util

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.cbgm.persistence.dao.ExerciseDao
import com.cbgm.persistence.entity.ExerciseStatus
import kotlinx.coroutines.delay

class UploadWorker(
    appContext: Context,
    params: WorkerParameters,
    private val exerciseDao: ExerciseDao
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val id = inputData.getLong("id", -1)
        if (id == -1L) return Result.failure()

        exerciseDao.updateStatus(id, ExerciseStatus.UPLOADING)

        for (p in 0..100 step 5) {
            delay(200)
            setProgress(workDataOf("progress" to p))
        }

        exerciseDao.updateStatus(id, ExerciseStatus.UPLOADED)
        return Result.success()
    }
}
