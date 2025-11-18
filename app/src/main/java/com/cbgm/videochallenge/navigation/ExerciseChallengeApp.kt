package com.cbgm.videochallenge.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.cbgm.videochallenge.presentation.MainViewModel
import com.cbgm.videochallenge.presentation.screen.overview.OverviewScreen
import com.cbgm.videochallenge.presentation.screen.playback.PlaybackScreen
import com.cbgm.videochallenge.presentation.screen.record.RecordingScreen

@Composable
fun ExerciseChallengeApp(mainViewModel: MainViewModel) {

    NavDisplay(
        backStack = mainViewModel.navigationFlow,
        transitionSpec = {
            fadeIn(tween(300)) togetherWith fadeOut(tween(300))
        },
        entryDecorators = listOf(),
        entryProvider = entryProvider {
            entry<Screen.Overview> {
                OverviewScreen()
            }
            entry<Screen.Detail> { key ->
                PlaybackScreen(key.id)
            }

            entry<Screen.Record> {
                RecordingScreen()
            }
        }
    )
}