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

@OptIn(ExperimentalPagingApi::class)
class ImageRemoteMediator(
    private val beerDb: ImageDatabase,
    private val imageApi: ImageApi
) : RemoteMediator<Int, ImageEntity>() {

    /*
    The Remote Mediator is like a helper that can go to the API and get us more beer information,
     but it only gets a little bit at a time.

     In our beer example, LoadType.REFRESH means we want to get all new beer information from the API and replace everything we have in
     our database with the new information.

     LoadType.PREPEND means we want to get a few new beer information from the API and add it to the beginning
      of the information we already have in our database.

     LoadType.APPEND means we want to get a few new beer information from the API and add it to the end of the information we already have in our database.

     So, LoadType is like a special code that tells the Remote Mediator how to get new beer information from the
     API and how to add it to the beer information we already have in our database.
     */

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
                        (lastItem.id?.trim()?.toInt()?.div(state.config.pageSize))?.plus(1) // defining nextPage
                    }
                }

                else -> {}
            }

            /*
            All of these database operations are grouped together into a transaction using beerDb.withTransaction { },
            so they will either all be executed successfully or none of them will be.
             */
            val beers = imageApi.getImages(page = loadKey as Int, pageCount = state.config.pageSize)
            beerDb.withTransaction {
                if (loadKey == LoadType.REFRESH) {
                    beerDb.dao.clearAll()
                }

                val beerEntities =
                    beers.map { it.toImageEntity() } // making a new collection and turning it into the entity to save in Room
                beerDb.dao.upsertAll(beerEntities)
            }

            MediatorResult.Success(
                endOfPaginationReached = beers.isEmpty() // if the list is empty it should stop paginating
            )
        } catch (ioException: IOException) {
            MediatorResult.Error(ioException)
        } catch (httpException: HttpException) {
            MediatorResult.Error(httpException)
        }
    }

}