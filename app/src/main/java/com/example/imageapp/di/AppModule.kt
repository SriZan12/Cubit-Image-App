package com.example.imageapp.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.example.imageapp.data.local.ImageDatabase
import com.example.imageapp.data.local.ImageEntity
import com.example.imageapp.data.remote.ImageApi
import com.example.imageapp.data.remote.ImageRemoteMediator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBeerDatabase(@ApplicationContext context: Context): ImageDatabase {
        return Room.databaseBuilder(
            context,
            ImageDatabase::class.java,
            "images.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideBeerApi(): ImageApi {
        return Retrofit.Builder()
            .baseUrl(ImageApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideBeerPager(imageDb: ImageDatabase, imageApi: ImageApi): Pager<Int, ImageEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = ImageRemoteMediator(
                beerDb = imageDb,
                imageApi = imageApi
            ),
            pagingSourceFactory = {
                imageDb.dao.pagingSource()
            }
        )
    }
}