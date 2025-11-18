package com.cbgm.videochallenge.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Screen : NavKey {
    @Serializable
    data object Overview : Screen
    @Serializable
    data object Record : Screen
    @Serializable
    data class Detail(val id: Long) : Screen
}