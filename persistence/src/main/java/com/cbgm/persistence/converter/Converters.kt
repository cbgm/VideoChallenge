package com.cbgm.persistence.converter

import androidx.room.TypeConverter
import com.cbgm.persistence.entity.ExerciseStatus

class Converters {
    @TypeConverter
    fun fromStatus(status: ExerciseStatus): String = status.name

    @TypeConverter
    fun toStatus(value: String): ExerciseStatus = ExerciseStatus.valueOf(value)
}