package com.cbgm.videochallenge.di

import com.cbgm.videochallenge.data.repository.ExerciseRepositoryImpl
import com.cbgm.videochallenge.domain.repository.ExerciseRepository
import com.cbgm.videochallenge.domain.usecase.AddRecordedExerciseUseCase
import com.cbgm.videochallenge.domain.usecase.ObserveExerciseUseCase
import com.cbgm.videochallenge.domain.usecase.ObserveExercisesPagedUseCase
import com.cbgm.videochallenge.domain.usecase.ObserveUploadUseCase
import com.cbgm.videochallenge.domain.usecase.UploadUseCase
import com.cbgm.videochallenge.navigation.Navigator
import com.cbgm.videochallenge.presentation.MainViewModel
import com.cbgm.videochallenge.presentation.screen.overview.OverviewViewModel
import com.cbgm.videochallenge.presentation.screen.playback.PlaybackViewModel
import com.cbgm.videochallenge.presentation.screen.record.RecordingViewModel
import com.cbgm.videochallenge.util.UploadWorker
import com.cbgm.videochallenge.util.VideoUtil
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<ExerciseRepository> { ExerciseRepositoryImpl(get(), get(), get()) }

    factory { ObserveExercisesPagedUseCase(get()) }
    factory { AddRecordedExerciseUseCase(get()) }
    factory { ObserveExerciseUseCase(get()) }
    factory { UploadUseCase(get()) }
    factory { ObserveUploadUseCase(get()) }

    single { Navigator() }
    single { VideoUtil(get()) }
    worker { UploadWorker(get(), get(), get()) }

    viewModel { OverviewViewModel(get(), get(), get()) }
    viewModel {
        PlaybackViewModel(
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel { RecordingViewModel(get(), get()) }
    viewModel { MainViewModel(get()) }
}
