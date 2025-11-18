package com.cbgm.videochallenge.data.repository

import android.content.Context
import android.net.Uri
import androidx.lifecycle.asFlow
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.cbgm.persistence.dao.ExerciseDao
import com.cbgm.persistence.entity.ExerciseStatus
import com.cbgm.videochallenge.data.mapper.toDomain
import com.cbgm.videochallenge.data.mapper.toEntity
import com.cbgm.videochallenge.domain.model.Exercise
import com.cbgm.videochallenge.domain.repository.ExerciseRepository
import com.cbgm.videochallenge.util.UploadWorker
import com.cbgm.videochallenge.util.VideoUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext

class ExerciseRepositoryImpl(
    private val context: Context,
    private val videoUtil: VideoUtil,
    private val dao: ExerciseDao,
) : ExerciseRepository {

    override fun observeExercisesPaged(): Flow<PagingData<Exercise>> {
        return Pager(PagingConfig(pageSize = 20)) {
            dao.pagingSource()
        }.flow.map { page -> page.map { it.toDomain() } }
    }

    override fun observe(id: Long): Flow<Exercise> =
        dao.observeExercise(id).map { entity -> entity.toDomain() }

    override suspend fun addExercise(uri: Uri): Boolean = withContext(Dispatchers.IO) {
        val file = videoUtil.copyUriToFile(uri)
        if (file != null) {
            val thumb = videoUtil.generateThumbnail(file)
            val ex = Exercise(
                uri = file.toURI().toString(),
                filename = file.name,
                timestamp = System.currentTimeMillis(),
                status = ExerciseStatus.RECORDED,
                thumbnailPath = thumb
            )
            dao.insert(ex.toEntity())
            return@withContext true
        }
        return@withContext false
    }

    override suspend fun uploadExercise(id: Long) {
        val request =
            OneTimeWorkRequestBuilder<UploadWorker>().setInputData(workDataOf("id" to id)).build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork("upload_$id", ExistingWorkPolicy.KEEP, request)
    }

    override fun getUploadProgress(id: Long): Flow<Int> =
        WorkManager.getInstance(context).getWorkInfosForUniqueWorkLiveData("upload_$id").asFlow()
            .mapNotNull { list ->
                list.firstOrNull()?.progress?.getInt("progress", -1)?.takeIf { it >= 0 }
            }.flowOn(Dispatchers.IO)

}