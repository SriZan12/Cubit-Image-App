package com.example.imageapp.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.imageapp.ui.vm.ImageVM


@Composable
fun ImageScreen(imageVM: ImageVM) {

    val imagesList = imageVM.imagePagerFlow.collectAsLazyPagingItems()

    val context = LocalContext.current
    LaunchedEffect(key1 = imagesList.loadState) {
        if (imagesList.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error: " + (imagesList.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 8.dp)
    ) {
        if (imagesList.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(
                    imagesList.itemCount,
                    key = { index -> imagesList[index]?.id ?: index }) { images ->
                    val imageItem = imagesList[images]
                    imageItem?.let { image ->
                        ImagesFeedItemCompo(
                            modifier = Modifier.fillMaxWidth(),
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
                item {
                    if (imagesList.loadState.append is LoadState.Loading) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
