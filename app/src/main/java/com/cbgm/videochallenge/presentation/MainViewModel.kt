package com.cbgm.videochallenge.presentation

import androidx.lifecycle.ViewModel
import com.cbgm.videochallenge.navigation.Navigator

class MainViewModel(
    navigator: Navigator

) : ViewModel() {
    val navigationFlow = navigator.backStack
}