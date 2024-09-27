package com.example.imageapp.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.imageapp.data.local.ImageDatabase
import com.example.imageapp.data.local.ImageEntity
import com.example.imageapp.data.mappers.toImageEntity
import okio.IOException
import retrofit2.HttpException


/**
 * @since The API does not support pagination, but infinite scrolling with pagination is required.
 * @see `page` below is defined to simulate pagination, allowing efficient loading of large lists.
 */

/**
 * @see ImageRemoteMediator class will act as the repository.
 * */

@OptIn(ExperimentalPagingApi::class)
class ImageRemoteMediator(
    private val imagesDB: ImageDatabase,
    private val imageApi: ImageApi
) : RemoteMediator<Int, ImageEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageEntity>
    ): MediatorResult {

        /**
         * @since api doesn't need to have query pages, the page is unused.
         * */
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull() ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
                (lastItem.id.trim().toInt()
                    .div(state.config.pageSize)) + 1 // defining the next page.
            }
        }

        return try {
            val response = imageApi.getImages()

            if (response.isSuccessful) {
                val images = response.body() ?: emptyList()

                imagesDB.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        imagesDB.dao.clearAll()
                    }
                    val imageEntities = images.map { it.toImageEntity() } // Map to entity
                    imagesDB.dao.upsertAll(imageEntities)
                }

                MediatorResult.Success(endOfPaginationReached = images.isEmpty())
            } else {
                MediatorResult.Error(Throwable("Network call failed: ${response.code()} ${response.message()}"))
            }
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}


