package com.cbgm.videochallenge.presentation.screen.playback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbgm.persistence.entity.ExerciseStatus
import com.cbgm.videochallenge.domain.usecase.ObserveExerciseUseCase
import com.cbgm.videochallenge.domain.usecase.ObserveUploadUseCase
import com.cbgm.videochallenge.domain.usecase.UploadUseCase
import com.cbgm.videochallenge.navigation.Navigator
import com.cbgm.videochallenge.presentation.mapper.toScreenEntity
import com.cbgm.videochallenge.presentation.model.ExerciseScreenEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch

data class PlaybackScreenState(
    val exercise: ExerciseScreenEntity? = null,
    val uploadProgress: Int = 0
)

class PlaybackViewModel(
    val navigator: Navigator,
    private val observeExerciseUseCase: ObserveExerciseUseCase,
    private val observeUploadUseCase: ObserveUploadUseCase,
    private val uploadUseCase: UploadUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(PlaybackScreenState())
    val state: StateFlow<PlaybackScreenState> = _state.asStateFlow()

    private var progressJob: Job? = null

    fun load(id: Long) {
        viewModelScope.launch {
            observeExerciseUseCase.invoke(id).collect { exercise ->
                _state.value = state.value.copy(
                    exercise = exercise.toScreenEntity()
                )
                if (exercise.status == ExerciseStatus.UPLOADING) {
                    observeProgress(exercise.id)
                }
            }
        }
    }

    fun upload() = viewModelScope.launch {
        val ex = _state.value.exercise
        ex?.let {
            uploadUseCase.invoke(it.id)
            observeProgress(it.id)
        }
    }

    private fun observeProgress(id: Long) {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            observeUploadUseCase.invoke(id).takeWhile { it < 100 }.collect { p ->
                _state.value = state.value.copy(uploadProgress = p)
            }
        }
    }

    fun onBack() {
        navigator.navigateBack()
    }
}