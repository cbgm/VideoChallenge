package com.cbgm.videochallenge.domain.usecase

import androidx.paging.PagingData
import com.cbgm.videochallenge.domain.model.Exercise
import com.cbgm.videochallenge.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow

class ObserveExercisesPagedUseCase(private val repo: ExerciseRepository) {
    operator fun invoke(): Flow<PagingData<Exercise>> = repo.observeExercisesPaged()
}