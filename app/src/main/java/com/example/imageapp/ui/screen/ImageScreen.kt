package com.example.imageapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.imageapp.components.ShimmerEffectItem
import com.example.imageapp.data.local.ImageEntity


@Composable
fun ImageScreen(imageList: LazyPagingItems<ImageEntity>) {

    ImageListCompo(imagesList = imageList)

}

@Composable
private fun ImageListCompo(imagesList: LazyPagingItems<ImageEntity>) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 8.dp)
    ) {

        val isLoading = imagesList.loadState.refresh is LoadState.Loading
        val isEmpty = imagesList.itemCount == 0 && !isLoading

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                items(10) {
                    ShimmerEffectItem()
                }

            } else {

                items(
                    imagesList.itemCount,
                    key = { index -> imagesList[index]?.id ?: index }) { images ->

                    val imageItem = imagesList[images]
                    imageItem?.let { image ->

                        ImagesFeedItemCompo(
                            modifier = Modifier
                                .animateItem()
                                .fillMaxWidth(),
                            imageEntity = image,
                            onLikeClick = {
                            },
                            onDislikeClick = {
                            },
                            onCommentClick = {
                            }
                        )
                    }
                }
            }
        }

    }
}




