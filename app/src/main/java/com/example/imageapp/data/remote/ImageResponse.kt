package com.example.imageapp.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageResponse(
    @SerialName("createdAt")
    val createdAt: String?,
    @SerialName("creator")
    val creator: Creator?,
    @SerialName("id")
    val id: String?,
    @SerialName("images")
    val images: List<String?>?,
    @SerialName("postText")
    val postText: String?
) {
    @Serializable
    data class Creator(
        @SerialName("avatar")
        val avatar: String?,
        @SerialName("firstName")
        val firstName: String?,
        @SerialName("lastName")
        val lastName: String?
    )
}
