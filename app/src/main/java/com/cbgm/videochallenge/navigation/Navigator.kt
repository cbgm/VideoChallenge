package com.cbgm.videochallenge.navigation

import androidx.compose.runtime.mutableStateListOf

class Navigator {
    val backStack = mutableStateListOf<Screen>(Screen.Overview)
    fun navigateTo(screen: Screen) {
        backStack.add(screen)
    }

    fun navigateBack() {
        backStack.removeLastOrNull()
    }
}