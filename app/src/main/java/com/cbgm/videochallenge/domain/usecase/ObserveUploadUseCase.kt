package com.cbgm.videochallenge.domain.usecase

import com.cbgm.videochallenge.domain.repository.ExerciseRepository

class ObserveUploadUseCase(private val repo: ExerciseRepository) {
    operator fun invoke(id: Long) = repo.getUploadProgress(id)
}