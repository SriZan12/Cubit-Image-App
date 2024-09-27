package com.example.imageapp.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface ImageApi {

    @GET("posts")
    suspend fun getImages(): Response<List<ImageResponse>>

    companion object {
        const val BASE_URL = "https://6335259f849edb52d6fc398e.mockapi.io/web-n-app-tasks/"
    }
}
