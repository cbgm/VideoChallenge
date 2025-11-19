package com.cbgm.videochallenge.presentation.screen.record

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecordingScreen(viewModel: RecordingViewModel = koinViewModel()) {
    RecordingScreen(
        saveRecorded = { uri -> viewModel.saveRecorded(uri) },
    )
}


@Composable
private fun RecordingScreen(
    saveRecorded: (Uri?) -> Unit,
) {
    val context = LocalContext.current

    val recorderLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            saveRecorded(result.data?.data)
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        if (perms.values.all { it }) {
            recorderLauncher.launch(Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30)
            })
        } else {
            Toast.makeText(context, "Camera and Microphone are required", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        val missing = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        ).filter { ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED }

        if (missing.isEmpty()) {
            recorderLauncher.launch(Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30)
            })
        } else {
            permissionLauncher.launch(missing.toTypedArray())
        }
    }
}