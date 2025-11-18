package com.cbgm.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cbgm.persistence.converter.Converters
import com.cbgm.persistence.dao.ExerciseDao
import com.cbgm.persistence.entity.ExerciseEntity

@Database(entities = [ExerciseEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
}