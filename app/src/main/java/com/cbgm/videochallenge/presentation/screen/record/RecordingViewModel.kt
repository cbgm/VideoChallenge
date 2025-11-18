package com.cbgm.videochallenge.presentation.screen.record

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbgm.videochallenge.domain.usecase.AddRecordedExerciseUseCase
import com.cbgm.videochallenge.navigation.Navigator
import kotlinx.coroutines.launch


class RecordingViewModel(
    val navigator: Navigator,
    val addUseCase: AddRecordedExerciseUseCase
) : ViewModel() {

    fun saveRecorded(uri: Uri) {
        viewModelScope.launch {
            addUseCase.invoke(uri)
        }
        onFinishRecording()
    }

    private fun onFinishRecording() {
        navigator.navigateBack()
    }
}