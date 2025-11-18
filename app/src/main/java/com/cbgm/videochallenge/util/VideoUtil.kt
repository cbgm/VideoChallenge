package com.cbgm.videochallenge.util

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import java.io.File

class VideoUtil(val context: Context) {

    fun copyUriToFile(srcUri: Uri): File? {
        return try {
            val input = context.contentResolver.openInputStream(srcUri) ?: return null
            val dir = File(context.filesDir, "videos").apply { if (!exists()) mkdirs() }
            val out = File(dir, "exercise_${System.currentTimeMillis()}.mp4")
            input.use { inputStream ->
                java.io.FileOutputStream(out).use { fos -> inputStream.copyTo(fos) }
            }
            out
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun generateThumbnail(file: File): String? {
        return try {
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(file.absolutePath)
            val bmp = mmr.getFrameAtTime(0) ?: return null
            val thumbDir = File(context.filesDir, "thumbs").apply { if (!exists()) mkdirs() }
            val out = File(thumbDir, "thumb_${file.nameWithoutExtension}.jpg")
            java.io.FileOutputStream(out)
                .use { fos -> bmp.compress(android.graphics.Bitmap.CompressFormat.JPEG, 80, fos) }
            out.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}