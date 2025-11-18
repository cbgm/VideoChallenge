package com.cbgm.videochallenge.domain.usecase

import com.cbgm.videochallenge.domain.repository.ExerciseRepository

class UploadUseCase(private val repo: ExerciseRepository) {
    suspend operator fun invoke(id: Long) = repo.uploadExercise(id)
}