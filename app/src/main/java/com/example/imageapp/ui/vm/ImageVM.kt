package com.example.imageapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.imageapp.data.local.ImageEntity
import com.example.imageapp.data.mappers.toImage

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ImageVM @Inject constructor(
    pager: Pager<Int, ImageEntity>
) : ViewModel() {

    val imagePagerFlow = pager
        .flow
        .map { data: PagingData<ImageEntity> ->
            data.map { it }
        }
        .cachedIn(viewModelScope)

    /*
    The .cachedIn(viewModelScope) part is like putting the image info after ImageVM is done watching with it,
    so that it can be easily retrieved later if needed.
     */
}