package com.cbgm.videochallenge.presentation.screen.record

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import org.koin.androidx.compose.koinViewModel
import java.io.File

@Composable
fun RecordingScreen(viewModel: RecordingViewModel = koinViewModel()) {
    RecordingScreen(
        saveRecorded = { uri -> viewModel.saveRecorded(uri) },
    )
}


@Composable
private fun RecordingScreen(
    saveRecorded: (Uri) -> Unit,
) {
    val context = LocalContext.current

    val recorderLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val uri: Uri = result.data?.data ?: run {
                // Emulator fallback with sample asset
                val file = File(context.cacheDir, "sample-2.mp4")
                if (!file.exists()) {
                    context.assets.open("sample-2.mp4").use { input ->
                        file.outputStream().use { output -> input.copyTo(output) }
                    }
                }
                file.toUri()
            }
            saveRecorded(uri)
        }

    val requestPermissions =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
            if (perms.values.all { it }) {
                val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30)
                recorderLauncher.launch(intent)
            }
        }

    LaunchedEffect(Unit) {
        requestPermissions.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Camera will start automatically once permissions are granted")
    }
}