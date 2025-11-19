package com.cbgm.videochallenge.presentation.screen.playback

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.cbgm.persistence.entity.ExerciseStatus
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PlaybackScreen(
    id: Long,
    viewModel: PlaybackViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(id) { viewModel.load(id) }

    PlaybackScreen(
        state = state,
        onUpload = { viewModel.upload() },
        onBack = { viewModel.onBack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaybackScreen(state: PlaybackScreenState, onUpload: () -> Unit, onBack: () -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        state.exercise?.let { e ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(paddingValues)
            ) {
                Text(e.filename, style = MaterialTheme.typography.displaySmall)
                Spacer(Modifier.height(8.dp))
                Text(
                    SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.GERMANY)
                        .format(java.util.Date(e.timestamp))
                )
                Spacer(Modifier.height(8.dp))
                Text("Status: ${e.status}")
                Spacer(Modifier.height(8.dp))
                VideoPlayer(uri = e.uri)
                Spacer(Modifier.height(12.dp))
                if (e.status == ExerciseStatus.UPLOADING) {
                    LinearProgressWithPercent(progress = state.uploadProgress)
                }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { onUpload() },
                    enabled = e.status.name == "RECORDED"
                ) {
                    Text("Upload")
                }
            }
        }
    }

}

@Composable
private fun VideoPlayer(uri: String) {
    val context = LocalContext.current
    val mediaItem = remember(uri) {
        if (uri.isNotEmpty()) {
            MediaItem.fromUri(uri)
        } else {
            MediaItem.fromUri(Uri.parse("asset:///sample-2.mp4"))
        }
    }
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = false
        }
    }
    DisposableEffect(Unit) { onDispose { player.release() } }

    AndroidView(
        factory = { ctx -> PlayerView(ctx).apply { this.player = player; useController = true } },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}

@Composable
private fun LinearProgressWithPercent(progress: Int) {
    Column {
        LinearProgressIndicator(progress = { progress / 100f }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(4.dp))
        Text("$progress%")
    }
}