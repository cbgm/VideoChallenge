package com.cbgm.persistence.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uri: String,
    val filename: String,
    val timestamp: Long,
    val status: ExerciseStatus,
    val thumbnailPath: String?
)

enum class ExerciseStatus { RECORDED, UPLOADING, UPLOADED }