package com.cbgm.videochallenge.domain.model

import com.cbgm.persistence.entity.ExerciseStatus

data class Exercise(
    val id: Long = 0L,
    val uri: String,
    val filename: String,
    val timestamp: Long,
    val status: ExerciseStatus,
    val thumbnailPath: String? = null
)