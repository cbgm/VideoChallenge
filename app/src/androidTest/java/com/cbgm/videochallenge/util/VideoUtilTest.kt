package com.cbgm.videochallenge.util

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class VideoUtilTest {

    private lateinit var videoUtil: VideoUtil
    private lateinit var testFile: File

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        videoUtil = VideoUtil(context)

        val dir = File(context.filesDir, "videos")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        testFile = File(dir, "sample-2.mp4")

        val assetManager = InstrumentationRegistry.getInstrumentation().context.assets
        assetManager.open("sample-2.mp4").use { inputStream ->
            testFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }

    @After
    fun teardown() {
        testFile.delete()
        val thumbFile = File(videoUtil.context.filesDir, "thumbs/thumb_test_video.jpg")
        if (thumbFile.exists()) {
            thumbFile.delete()
        }
    }

    @Test
    fun `copyUriToFile should copy file when uri is valid`() {
        val uri = Uri.fromFile(testFile)
        val copiedFile = videoUtil.copyUriToFile(uri)
        assertNotNull(copiedFile)
        assertTrue(copiedFile!!.exists())
        copiedFile.delete()
    }

    @Test
    fun `generateThumbnail should create thumbnail when file is valid`() {
        val thumbnailPath = videoUtil.generateThumbnail(testFile)
        assertNotNull(thumbnailPath)
        val thumbnailFile = File(thumbnailPath!!)
        assertTrue(thumbnailFile.exists())
    }
}
