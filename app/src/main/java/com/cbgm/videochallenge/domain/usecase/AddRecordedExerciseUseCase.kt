package com.cbgm.videochallenge.domain.usecase

import android.net.Uri
import com.cbgm.videochallenge.domain.repository.ExerciseRepository

class AddRecordedExerciseUseCase(private val repo: ExerciseRepository) {
    suspend operator fun invoke(uri: Uri?) = repo.addExercise(uri)
}