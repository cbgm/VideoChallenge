package com.cbgm.videochallenge.data.mapper

import com.cbgm.persistence.entity.ExerciseEntity
import com.cbgm.videochallenge.domain.model.Exercise

fun ExerciseEntity.toDomain() = Exercise(
    id = id,
    uri = uri,
    filename = filename,
    timestamp = timestamp,
    status = status,
    thumbnailPath = thumbnailPath
)

fun Exercise.toEntity() = ExerciseEntity(
    id = id,
    uri = uri,
    filename = filename,
    timestamp = timestamp,
    status = status,
    thumbnailPath = thumbnailPath
)