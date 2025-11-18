package com.cbgm.persistence.di

import androidx.room.Room
import com.cbgm.persistence.AppDatabase
import org.koin.dsl.module

val persistenceModule = module {
    single { Room.databaseBuilder(get(), AppDatabase::class.java, "app_db").build() }
    single { get<AppDatabase>().exerciseDao() }
}