package com.example.imageapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val creatorName: String?,
    val avatar: String?,
    val postText: String?,
    val imageUrls: String?,
    val createdAt: String?
)