package com.cbgm.videochallenge.domain.usecase

import com.cbgm.videochallenge.domain.model.Exercise
import com.cbgm.videochallenge.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow

class ObserveExerciseUseCase(private val repo: ExerciseRepository) {
    operator fun invoke(id: Long): Flow<Exercise> = repo.observe(id)
}