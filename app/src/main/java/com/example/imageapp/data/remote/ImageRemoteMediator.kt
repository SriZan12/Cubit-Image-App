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
        return try {
            val loadKey = when (loadType) {

                LoadType.REFRESH -> 1 // starting all over from the initial page
                LoadType.PREPEND -> {
                    return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()

                    if (lastItem == null) {
                        1
                    } else {
                        (lastItem.id?.trim()?.toInt()
                            ?.div(state.config.pageSize))?.plus(1) // defining nextPage
                    }
                }

                else -> {}
            }

            /*
            All of these database operations are grouped together into a transaction using imagesDB.withTransaction { },
            so they will either all be executed successfully or none of them will be.
             */
//            val images = imageApi.getImages(page = 1, pageCount = state.config.pageSize)
            val images = imageApi.getImages()
            imagesDB.withTransaction {
                if (loadKey == LoadType.REFRESH) {
                    imagesDB.dao.clearAll()
                }

                val imagesEntities =
                    images.map { it.toImageEntity() } // making a new collection and turning it into the entity to save in Room
                imagesDB.dao.upsertAll(imagesEntities)
            }

            MediatorResult.Success(
                endOfPaginationReached = images.isEmpty() // if the list is empty it should stop paginating
            )
        } catch (ioException: IOException) {
            MediatorResult.Error(ioException)
        } catch (httpException: HttpException) {
            MediatorResult.Error(httpException)
        }
    }

}

