package com.cbgm.videochallenge.presentation.model

import androidx.compose.runtime.Stable
import com.cbgm.persistence.entity.ExerciseStatus

@Stable
data class ExerciseScreenEntity(
    val id: Long,
    val uri: String,
    val filename: String,
    val timestamp: Long,
    val thumbnailPath: String?,
    val status: ExerciseStatus,
)