package com.example.imageapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.imageapp.data.local.ImageEntity

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * @see flow used in PagingData is inherently reactive. It allows for asynchronous streams of data,
 * where the UI reacts to changes as new pages of data are fetched.
 *
 * @see cachedIn(viewModelScope) is like putting the image info after ImageVM is done watching with it.
 */

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
}