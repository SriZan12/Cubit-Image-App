package com.example.imageapp.ui.screen
/*

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import coil.request.ImageRequest
import com.example.imageapp.data.remote.ImageResponse

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


@Composable
private fun ImageBeforeLikeDislikeComment(
    images: List<String?>,
    likeCount: Int,
    dislikeCount: Int,
    onLikeClick: () -> Unit,
    onDislikeClick: () -> Unit,
    ownReaction: String,
    commentCount: Int,
    onCommentClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLikeEnabled: Boolean = true,
    isDislikeEnabled: Boolean = true
) {
    val lazyRowState = rememberLazyListState()
    val snapFling = rememberSnapFlingBehavior(lazyRowState)

    var isLiked by rememberSaveable(ownReaction) { mutableStateOf(ownReaction == "Like") }
    var isDisliked by rememberSaveable(ownReaction) { mutableStateOf(ownReaction == "Dislike") }

    var likeCounter by rememberSaveable(likeCount) { mutableIntStateOf(likeCount) }
    var dislikeCounter by rememberSaveable(likeCount) { mutableIntStateOf(dislikeCount) }

    Column(modifier = modifier) {
        LazyRow(
            modifier = Modifier
                .fillMaxSize()
                .height(200.dp),
            state = lazyRowState,
            flingBehavior = snapFling
        ) {
            items(items = images) { image ->
                AsyncImage(
                    modifier = Modifier
                        .fillParentMaxSize()
                        .clip(MaterialTheme.shapes.large)
                        .clickable(onClick = onCommentClick),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    model = ImageRequest.Builder(LocalContext.current).data(image)
                        .crossfade(true)
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
                icon = if (isLiked) Res.drawable.like_filled_icon else Res.drawable.like,
                iconDescription = "Like",
                counter = likeCounter,
                buttonColors = IconButtonDefaults.iconButtonColors().copy(
                    contentColor = if (isLiked) Color(0xFF3B5999) else brand_gray_color,
                    disabledContentColor = if (isLiked) Color(0xFF3B5999) else brand_gray_color
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
                icon = if (isDisliked) Res.drawable.like_filled_icon else Res.drawable.like,
                iconDescription = "Dislike",
                counter = dislikeCounter,
                buttonColors = IconButtonDefaults.iconButtonColors().copy(
                    contentColor = if (isDisliked) Color(0xFF3B5999) else brand_gray_color,
                    disabledContentColor = if (isDisliked) Color(0xFF3B5999) else brand_gray_color
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
                icon = Res.drawable.comments,
                iconDescription = "Tap to read comments",
                counter = commentCount,
                onClick = onCommentClick
            )
        }
    }
}


@Composable
fun ImagesFeedItemCompo(
    modifier: Modifier = Modifier,
    imageResponse: ImageResponse,
    onLikeClick: () -> Unit,
    onDislikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    isLikeEnabled: Boolean = true,
    isDislikeEnabled: Boolean = true,
    cardColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest
//    cardColor: Color = brand_primary_green_50_color
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors().copy(containerColor = cardColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
//            modifier = Modifier.fillMaxWidth().padding(16.dp).animateContentSize()
        ) {
            UserProfileWithCropItemTag(
                modifier = Modifier.fillMaxWidth(),
                userName = "${imageResponse.creator?.firstName} ${imageResponse.creator?.firstName}"
                    ?: "User",
                profilePicture = imageResponse.creator?.avatar ?: "",
                uploadedDateAndTime = imageResponse.createdAt ?: "",
                itemName = imageResponse.postText ?: "",
                itemImage = imageResponse.images?.get(1) ?: imageResponse.images?.get(0),
            )

            if (!imageResponse.postText.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp, top = 4.dp),
                    text = imageResponse.postText,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = ExtendedTheme.colors.customLabelColor.color
                )
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }

            ImageBeforeLikeDislikeComment(
                modifier = Modifier,
                images = imageResponse.images ?: emptyList(),
                likeCount = 0,
                dislikeCount = 0,
                ownReaction = "",
                onLikeClick = onLikeClick,
                onDislikeClick = onDislikeClick,
                isLikeEnabled = isLikeEnabled,
                isDislikeEnabled = isDislikeEnabled,
                commentCount = 0,
                onCommentClick = onCommentClick
            )
        }
    }
}


@Composable
private fun UserProfileWithCropItemTag(
    profilePicture: String,
    userName: String,
    uploadedDateAndTime: String,
    itemName: String,
    itemImage: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.padding(bottom = 6.dp),
//            modifier = Modifier.padding(top = 4.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (profilePicture.isEmpty()) {
                Image(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    painter = painterResource(Res.drawable.dummy_profile2),
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
                    placeholder = painterResource(Res.drawable.dummy_profile2),
                    error = painterResource(Res.drawable.dummy_profile2),
                    fallback = painterResource(Res.drawable.dummy_profile2)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically)
            ) {
                Text(
                    text = userName, style = TextStyle(
                        fontSize = 16.sp, fontWeight = FontWeight.Medium
                    ), color = ExtendedTheme.colors.customLabelColor.color
                )
                Text(
                    text = DateAndTimeUtils.getRelativeTime(uploadedDateAndTime),
                    style = TextStyle(
                        fontSize = 12.sp
                    ), color = brand_neutral_gray_color
                )
            }
        }

        if (itemName.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors().copy(
                    containerColor = brand_geokrishi_color
                ), shape = CircleShape
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (!itemImage.isNullOrEmpty()) {
                        AsyncImage(
                            modifier = Modifier.size(24.dp),
                            model = itemImage,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                    Text(
                        modifier = Modifier,
                        text = itemName,
                        style = MaterialTheme.typography.labelLarge,
                        color = brand_dark_geokrishi_color
                    )
                }
            }
        }
    }
}*/
