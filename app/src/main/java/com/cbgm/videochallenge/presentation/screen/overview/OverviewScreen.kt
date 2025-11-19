package com.cbgm.videochallenge.presentation.screen.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.cbgm.persistence.entity.ExerciseStatus
import com.cbgm.videochallenge.presentation.model.ExerciseScreenEntity
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun OverviewScreen(
    viewModel: OverviewViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val exercisesPaged = state.exercisesFlow.collectAsLazyPagingItems()
    val progressMap by viewModel.uploadProgressMap.collectAsStateWithLifecycle()

    OverviewScreen(
        exercisesPaged = exercisesPaged,
        progressMap = progressMap,
        onDetail = { id -> viewModel.onDetail(id) },
        onRecord = { viewModel.onRecord() },
        onObserveProgress = { id -> viewModel.observeUploadProgress(id) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OverviewScreen(
    exercisesPaged: LazyPagingItems<ExerciseScreenEntity>,
    progressMap: Map<Long, Int>,
    onRecord: () -> Unit,
    onDetail: (Long) -> Unit,
    onObserveProgress: (Long) -> Unit
) {

    val listState = rememberLazyListState()
    var previousItemCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(exercisesPaged.itemCount) {
        if (exercisesPaged.itemCount > previousItemCount) {
            listState.animateScrollToItem(0)
        }
        previousItemCount = exercisesPaged.itemCount
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Exercises") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onRecord() }) { Text("Rec") }
        }
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier.padding(padding)
        ) {
            items(
                count = exercisesPaged.itemCount,
                key = { index -> exercisesPaged[index]?.id ?: index }
            ) { index ->
                exercisesPaged[index]?.let { ex ->
                    ExerciseRow(
                        exercise = ex,
                        progressMap = progressMap,
                        onClick = { onDetail(ex.id) },
                        onObserveProgress = { onObserveProgress(ex.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ExerciseRow(
    exercise: ExerciseScreenEntity,
    progressMap: Map<Long, Int>,
    onClick: () -> Unit,
    onObserveProgress: () -> Unit
) {
    if (exercise.status == ExerciseStatus.UPLOADING) {
        LaunchedEffect(exercise.id) { onObserveProgress() }
    }

    val progress by remember(exercise.id, progressMap) {
        derivedStateOf { progressMap[exercise.id] ?: 0 }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (exercise.uri.isEmpty()) Color.LightGray else Color.White)
            .clickable { onClick() }
            .padding(8.dp))
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Box(modifier = Modifier.size(96.dp)) {
                val thumb = exercise.thumbnailPath
                if (thumb != null) {
                    AsyncImage(
                        model = thumb,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                }
            }

            Spacer(Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(exercise.filename)
                Text(
                    SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.GERMANY)
                        .format(java.util.Date(exercise.timestamp))
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(exercise.status.name)
                if (exercise.status == ExerciseStatus.UPLOADED) {
                    Text("✔")
                }
            }
        }
        if (exercise.status == ExerciseStatus.UPLOADING) {
            LinearProgressIndicator(
                progress = { progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }
    }
}

@Preview
@Composable
fun OverviewScreenPreview() {

    val sampleExercises = listOf(
        ExerciseScreenEntity(
            id = 1,
            filename = "Exercise 1",
            timestamp = System.currentTimeMillis(),
            thumbnailPath = null,
            status = ExerciseStatus.UPLOADED,
            uri = "sadffsf"
        ),
        ExerciseScreenEntity(
            id = 2,
            filename = "Exercise 2",
            timestamp = System.currentTimeMillis(),
            thumbnailPath = null,
            status = ExerciseStatus.RECORDED,
            uri = "sadffsf"
        ),
    )

    val pagingDataFlow = remember { flowOf(PagingData.from(sampleExercises)) }
    val exercisesPaged = pagingDataFlow.collectAsLazyPagingItems()

    OverviewScreen(
        exercisesPaged = exercisesPaged,
        progressMap = emptyMap(),
        onDetail = {},
        onRecord = {},
        onObserveProgress = {}
    )
}