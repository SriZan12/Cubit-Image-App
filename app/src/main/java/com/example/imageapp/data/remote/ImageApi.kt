package com.example.imageapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApi {

    @GET("beers")
    suspend fun getImages(
        @Query("page") page: Int,
        @Query("per_page") pageCount: Int
    ): List<ImageResponse>

    companion object {
        const val BASE_URL = "https://6335259f849edb52d6fc398e.mockapi.io/web-n-app-tasks/posts"
    }
}