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
import java.io.File
import java.util.UUID

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

    override suspend fun addExercise(uri: Uri?): Boolean = withContext(Dispatchers.IO) {
        val (savedUri, filename, thumbPath) = saveToLocalStorage(uri) ?: return@withContext false
        dao.insert(
            Exercise(
                uri = savedUri,
                filename = filename,
                timestamp = System.currentTimeMillis(),
                status = ExerciseStatus.RECORDED,
                thumbnailPath = thumbPath
            ).toEntity()
        )
        return@withContext true
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

    private fun saveToLocalStorage(uri: Uri?): Triple<String, String, String>? {
        val (fileForThumb, savedUri, filename) = if (uri != null) {
            val file = videoUtil.copyUriToFile(uri)
                ?: return saveToLocalStorage(null)

            Triple(file, file.toURI().toString(), file.name)

        } else {
            val input = context.assets.open("sample-2.mp4")
            val temp = File.createTempFile(
                "thumb_asset_${UUID.randomUUID()}",
                ".mp4",
                context.cacheDir
            ).apply {
                outputStream().use { out -> input.copyTo(out) }
            }

            Triple(temp, "", "sample-2.mp4")
        }

        val thumbPath = videoUtil.generateThumbnail(fileForThumb)

        if (uri == null) fileForThumb.delete()
        if (thumbPath == null) return null

        return Triple(savedUri, filename, thumbPath)
    }


}