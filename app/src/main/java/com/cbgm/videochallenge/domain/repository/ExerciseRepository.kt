package com.cbgm.videochallenge.domain.repository

import android.net.Uri
import androidx.paging.PagingData
import com.cbgm.videochallenge.domain.model.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    fun observeExercisesPaged(): Flow<PagingData<Exercise>>
    fun observe(id: Long): Flow<Exercise>
    suspend fun addExercise(uri: Uri): Boolean
    fun getUploadProgress(id: Long): Flow<Int>
    suspend fun uploadExercise(id: Long)
}