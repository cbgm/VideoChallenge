package com.cbgm.videochallenge.presentation.screen.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.cbgm.videochallenge.domain.usecase.ObserveExercisesPagedUseCase
import com.cbgm.videochallenge.domain.usecase.ObserveUploadUseCase
import com.cbgm.videochallenge.navigation.Navigator
import com.cbgm.videochallenge.navigation.Screen
import com.cbgm.videochallenge.presentation.mapper.toScreenEntity
import com.cbgm.videochallenge.presentation.model.ExerciseScreenEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OverviewScreenState(
    val exercisesFlow: Flow<PagingData<ExerciseScreenEntity>>
)

class OverviewViewModel(
    val navigator: Navigator,
    observePaged: ObserveExercisesPagedUseCase,
    val observeUploadUseCase: ObserveUploadUseCase,
) : ViewModel() {

    private val _state =
        MutableStateFlow(OverviewScreenState(exercisesFlow = flowOf(PagingData.empty())))
    val state: StateFlow<OverviewScreenState> = _state.asStateFlow()

    private val _uploadProgressMap = MutableStateFlow<Map<Long, Int>>(emptyMap())
    val uploadProgressMap: StateFlow<Map<Long, Int>> = _uploadProgressMap

    init {
        val mappedFlow = observePaged()
            .map { pagingData ->
                pagingData.map { ex -> ex.toScreenEntity() }
            }
            .cachedIn(viewModelScope)

        _state.value = _state.value.copy(exercisesFlow = mappedFlow)
    }

    fun onDetail(id: Long) = navigator.navigateTo(Screen.Detail(id))
    fun onRecord() = navigator.navigateTo(Screen.Record)

    fun observeUploadProgress(id: Long) {
        viewModelScope.launch {
            observeUploadUseCase.invoke(id).collect { progress ->
                _uploadProgressMap.update { it + (id to progress) }
            }
        }
    }
}