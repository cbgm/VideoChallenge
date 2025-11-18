package com.cbgm.videochallenge.presentation.mapper

import com.cbgm.videochallenge.domain.model.Exercise
import com.cbgm.videochallenge.presentation.model.ExerciseScreenEntity


fun Exercise.toScreenEntity() = ExerciseScreenEntity(
    id = id,
    uri = uri,
    filename = filename,
    timestamp = timestamp,
    status = status,
    thumbnailPath = thumbnailPath,
)