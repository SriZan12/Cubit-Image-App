package com.example.imageapp.ui.screen


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.imageapp.R
import com.example.imageapp.data.local.ImageEntity
import com.example.imageapp.util.DateAndTimeUtils
import com.example.imageapp.util.NumberUtils
import kotlinx.serialization.json.Json

@Composable
fun ImagesFeedItemCompo(
    modifier: Modifier = Modifier,
    imageEntity: ImageEntity,
    onLikeClick: () -> Unit,
    onDislikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    isLikeEnabled: Boolean = true,
    isDislikeEnabled: Boolean = true,
    cardColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest
) {

    var visibility by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        visibility = true
    }

    AnimatedVisibility(
        visible = visibility,
        enter = slideInHorizontally(),
        exit = slideOutHorizontally(),
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors().copy(containerColor = cardColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Log.d("AVATAR", "AVATAR = ${imageEntity.avatar}")

                UserProfileItem(
                    modifier = Modifier.fillMaxWidth(),
                    userName = imageEntity.creatorName ?: "User",
                    imageEntity = imageEntity,
                    profilePicture = imageEntity.avatar ?: "",
                    uploadedDateAndTime = imageEntity.createdAt ?: "",
                )

                if (!imageEntity.postText.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier.padding(
                            bottom = 8.dp,
                            top = 4.dp,
                            start = 4.dp,
                            end = 4.dp
                        ),
                        text = imageEntity.postText,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 3,
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Normal,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black
                    )
                } else {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                val imagesList = remember(imageEntity.imageUrls) {
                    Json.decodeFromString<List<String>>(imageEntity.imageUrls ?: "[]")
                }
                Log.d("IMAGES LIST = ", "IMAGES LIST = $imagesList")

                FeedImage(
                    modifier = Modifier,
                    images = imagesList,
                    onLikeClick = onLikeClick,
                    onDislikeClick = onDislikeClick,
                    isLikeEnabled = isLikeEnabled,
                    isDislikeEnabled = isDislikeEnabled,
                    onCommentClick = onCommentClick
                )
            }
        }
    }


}

/**
 * @see likesCount, disLikesCount and commentsCount are randomly generated.
 * */
@Composable
private fun FeedImage(
    modifier: Modifier = Modifier,
    images: List<String?>,
    onLikeClick: () -> Unit,
    onDislikeClick: () -> Unit,
    ownReaction: String = "",
    onCommentClick: () -> Unit,
    isLikeEnabled: Boolean = true,
    isDislikeEnabled: Boolean = true
) {
    val lazyRowState = rememberLazyListState()
    val snapFling = rememberSnapFlingBehavior(lazyRowState)

    val randomLikesAndComments =
        rememberSaveable { NumberUtils.generateRandomLikesAndComments() }

    var isLiked by rememberSaveable(ownReaction) { mutableStateOf(ownReaction == "Like") }
    var isDisliked by rememberSaveable(ownReaction) { mutableStateOf(ownReaction == "Dislike") }

    var likeCounter by rememberSaveable { mutableIntStateOf(randomLikesAndComments.first) }
    var dislikeCounter by rememberSaveable { mutableIntStateOf(randomLikesAndComments.second.first) }
    val commentCounter = rememberSaveable { randomLikesAndComments.second.second }

    Column(modifier = modifier) {
        LazyRow(
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize()
                .height(200.dp),
            state = lazyRowState,
            flingBehavior = snapFling
        ) {
            itemsIndexed(items = images) { _, image ->
                Log.d("IMAGE OF THE APP", "IMAGE OF THE APP${image.toString()}")
//                val uniqueImageUrl = "${image}?postId=${index}"
                AsyncImage(
                    modifier = Modifier
                        .fillParentMaxSize()
                        .clip(MaterialTheme.shapes.large)
                        .clickable(onClick = onCommentClick),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    model = ImageRequest.Builder(LocalContext.current).data(image)
                        .crossfade(true)
                        .memoryCachePolicy(CachePolicy.DISABLED) // Disable memory cache
                        .diskCachePolicy(CachePolicy.DISABLED)
                        .build()
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ClickableIconWithCounters(
                icon = if (isLiked) R.drawable.liked else R.drawable.like,
                iconDescription = "Like",
                counter = likeCounter,
                buttonColors = IconButtonDefaults.iconButtonColors().copy(
                    contentColor = if (isLiked) Color(0xFF3B5999) else Color.Gray,
                    disabledContentColor = if (isLiked) Color(0xFF3B5999) else Color.Gray
                ),
                onClick = {
                    onLikeClick()

                    if (!isLiked) {
                        likeCounter++
                        if (isDisliked) {
                            dislikeCounter--
                        }
                    }

                    isLiked = true
                    isDisliked = false
                },
                enabled = isLikeEnabled
            )

            ClickableIconWithCounters(
                iconModifier = Modifier.rotate(180f),
                icon = if (isDisliked) R.drawable.liked else R.drawable.like,
                iconDescription = "Dislike",
                counter = dislikeCounter,
                buttonColors = IconButtonDefaults.iconButtonColors().copy(
                    contentColor = if (isDisliked) Color(0xFF3B5999) else Color.Gray,
                    disabledContentColor = if (isDisliked) Color(0xFF3B5999) else Color.Gray
                ),
                onClick = {
                    onDislikeClick()

                    if (!isDisliked) {
                        dislikeCounter++
                        if (isLiked) {
                            likeCounter--
                        }
                    }

                    isDisliked = true
                    isLiked = false
                },
                enabled = isDislikeEnabled
            )

            ClickableIconWithCounters(
                icon = R.drawable.comment,
                iconDescription = "Tap to read comments",
                counter = commentCounter,  // Use the fixed commentCounter value
                onClick = onCommentClick
            )
        }
    }
}


@Composable
private fun UserProfileItem(
    profilePicture: String,
    userName: String,
    imageEntity: ImageEntity,
    uploadedDateAndTime: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.padding(bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Log.d("INSIDE PROFILE PIC", "INSIDE PROFILE PIC = ${imageEntity.avatar}")
            if (profilePicture.isEmpty()) {
                Image(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    painter = painterResource(R.drawable.dummy_profile),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            } else {
                AsyncImage(
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape),
                    contentDescription = null,
                    model = ImageRequest.Builder(LocalContext.current).data(profilePicture)
                        .crossfade(true).build(),
                    placeholder = painterResource(R.drawable.dummy_profile),
                    error = painterResource(R.drawable.dummy_profile),
                    fallback = painterResource(R.drawable.dummy_profile)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically)
            ) {
                Text(
                    text = userName, style = TextStyle(
                        fontSize = 16.sp, fontWeight = FontWeight.Medium
                    ), color = Color.Black
                )
                Text(
                    text = DateAndTimeUtils.getDateFromTimestamp(uploadedDateAndTime),
                    style = TextStyle(
                        fontSize = 12.sp
                    ), color = Color.Black
                )
            }
        }

    }
}

@Composable
fun ClickableIconWithCounters(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: Int,
    iconDescription: String,
    counter: Int,
    enabled: Boolean = true,
    textColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    buttonColors: IconButtonColors = IconButtonDefaults.iconButtonColors(
        contentColor = Color.Gray,
        disabledContentColor = Color.Gray
    )
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onClick,
            colors = buttonColors,
            enabled = enabled
        ) {
            Icon(
                modifier = iconModifier.size(23.dp),
                painter = painterResource(icon),
                contentDescription = iconDescription
            )
        }

        Text(
            modifier = Modifier.offset(x = -(4).dp),
            text = counter.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
        )
    }
}


