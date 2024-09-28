package com.example.imageapp

import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.imageapp.data.local.ImageDao
import com.example.imageapp.data.local.ImageDatabase
import com.example.imageapp.data.local.ImageEntity
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import kotlin.jvm.Throws

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(AndroidJUnit4::class)
class ImageDaoTest {
    private lateinit var database: ImageDatabase
    private lateinit var dao: ImageDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ImageDatabase::class.java
        ).build()
        dao = database.dao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun upsertAll_insertsNewImages() = runBlocking {
        val images = listOf(
            ImageEntity(
                id = 1,
                creatorName = "Alice",
                avatar = "url1",
                postText = "Text1",
                imageUrls = "url1",
                createdAt = "2024-01-01"
            ),
            ImageEntity(
                id = 2,
                creatorName = "Bob",
                avatar = "url2",
                postText = "Text2",
                imageUrls = "url2",
                createdAt = "2024-01-02"
            )
        )

        dao.upsertAll(images)


        val retrievedImages = dao.getAllImages()

        retrievedImages.forEach { image ->
            Log.d("RETRIEVED IMAGES", "IMAGE = ${image}")
        }
        assertEquals(images.size, retrievedImages.size)
        val testImagesContains = retrievedImages.containsAll(images)
        assertTrue(testImagesContains)
    }

    @Test
    fun clearAll_deletesAllImages() = runBlocking {
        val images = listOf(
            ImageEntity(
                id = 1,
                creatorName = "Alice",
                avatar = "url1",
                postText = "Text1",
                imageUrls = "url1",
                createdAt = "2024-01-01"
            )
        )

        dao.upsertAll(images)

        val initialResult = dao.getAllImages()
        assertEquals(1, initialResult.size)

        dao.clearAll()

        val finalResult = dao.getAllImages()
        assertTrue(finalResult.isEmpty())
    }

}