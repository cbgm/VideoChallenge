package com.cbgm.persistence.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cbgm.persistence.entity.ExerciseEntity
import com.cbgm.persistence.entity.ExerciseStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises ORDER BY timestamp DESC")
    fun pagingSource(): PagingSource<Int, ExerciseEntity>

    @Query("SELECT * FROM exercises WHERE id = :id")
    fun observeExercise(id: Long): Flow<ExerciseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(e: ExerciseEntity): Long

    @Query("UPDATE exercises SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: ExerciseStatus)

    @Query("SELECT * FROM exercises WHERE id = :id")
    suspend fun get(id: Long): ExerciseEntity?
}