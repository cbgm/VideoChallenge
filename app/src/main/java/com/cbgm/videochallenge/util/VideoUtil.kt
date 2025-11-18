package com.cbgm.videochallenge.util

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

class VideoUtil(val context: Context) {

    fun copyUriToFile(srcUri: Uri): File? {
        return runCatching {
            val input = context.contentResolver.openInputStream(srcUri) ?: return null

            val dir = File(context.filesDir, "videos").apply { mkdirs() }
            val outFile = File(dir, "exercise_${System.currentTimeMillis()}.mp4")

            input.use { inputStream ->
                FileOutputStream(outFile).use { fos ->
                    inputStream.copyTo(fos)
                }
            }

            outFile
        }.getOrElse {
            it.printStackTrace()
            null
        }
    }

    fun generateThumbnail(file: File): String? {
        return runCatching {
            MediaMetadataRetriever().use { retriever ->
                retriever.setDataSource(file.absolutePath)
                val bitmap = retriever.getFrameAtTime(0) ?: return null

                val thumbDir = File(context.filesDir, "thumbs").apply { mkdirs() }
                val outputFile = File(thumbDir, "thumb_${file.nameWithoutExtension}.jpg")

                FileOutputStream(outputFile).use { fos ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)
                }

                outputFile.absolutePath
            }
        }.getOrElse {
            it.printStackTrace()
            null
        }
    }
}