package com.example.imageapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ImageDao {

    @Upsert
    suspend fun upsertAll(images: List<ImageEntity>)

    @Query("SELECT * FROM ImageEntity")
    suspend fun getAllImages(): List<ImageEntity>

    @Query("SELECT * FROM ImageEntity")
    fun pagingSource(): PagingSource<Int, ImageEntity>
//     PagingSource is like a helper that knows how to get the data in small chunks so we can show it on our screen
    //     without loading everything at once.

    @Query("DELETE FROM ImageEntity")
    suspend fun clearAll()
}