package com.example.imageapp.data.mappers

import com.example.imageapp.data.local.ImageEntity
import com.example.imageapp.data.remote.ImageResponse


fun ImageResponse.toImageEntity(): ImageEntity {
    return ImageEntity(
        id = id.toString(),
        creatorName = "${creator?.firstName} ${creator?.lastName}",
        avatar = creator?.avatar,
        postText = postText,
        imageUrls = images?.get(0) ?: images?.get(1),
        createdAt = createdAt
    )
}
