package com.example.imageapp

import com.example.imageapp.data.remote.ImageApi
import com.google.gson.Gson
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.converter.moshi.MoshiConverterFactory


class ImageApiTest {

    private lateinit var server: MockWebServer

    private lateinit var imageApi: ImageApi

    @Before
    fun setup() {
        server = MockWebServer()
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        imageApi = retrofit.create(ImageApi::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun getImages_success() = runTest {
        val gson = Gson()

//            gson.toJson(
//            listOf(
//                ImageResponse(
//                    "2024-01-01",
//                    ImageResponse.Creator("url1", "Alice", "Smith"),
//                    "1",
//                    listOf("url1"),
//                    "Text1"
//                ),
//                ImageResponse(
//                    "2024-01-02",
//                    ImageResponse.Creator("url2", "Bob", "Johnson"),
//                    "2",
//                    listOf("url2"),
//                    "Text2"
//                )
//            )
//        )

        server.enqueue(MockResponse().setBody("[]"))

        val response = imageApi.getImages()
        server.takeRequest()

        assertTrue(response.isSuccessful)
//        assertEquals(2, response.body()?.size)
    }
}

