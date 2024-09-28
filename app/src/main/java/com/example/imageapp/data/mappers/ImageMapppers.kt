package com.example.imageapp.data.mappers

import com.example.imageapp.data.local.ImageEntity
import com.example.imageapp.data.remote.ImageResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


fun ImageResponse.toImageEntity(): ImageEntity {
    return ImageEntity(
        id = id.toString(),
        creatorName = "${creator?.firstName} ${creator?.lastName}",
        avatar = creator?.avatar,
        postText = postText,
        imageUrls = Json.encodeToString(images),
        createdAt = createdAt
    )
}