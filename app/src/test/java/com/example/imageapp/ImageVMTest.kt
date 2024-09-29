package com.example.imageapp

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.example.imageapp.data.local.ImageEntity
import com.example.imageapp.ui.vm.ImageVM
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class ImageVMTest {

    @Mock
    private lateinit var pager: Pager<Int, ImageEntity>

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun imagePagerFlow_emitsData() = runTest(testDispatcher) {
        // Arrange
        val mockImages = listOf(
            ImageEntity(1, "Alice", "url1", "Text1", "url1", "2024-01-01"),
            ImageEntity(2, "Bob", "url2", "Text2", "url2", "2024-01-02")
        )
        val mockPagingData = PagingData.from(mockImages)
        `when`(pager.flow).thenReturn(flowOf(mockPagingData))

        val viewModel = ImageVM(pager)

        // Act
        val result = viewModel.imagePagerFlow.toList()

        // Advancing time to ensure the value is collected
        advanceUntilIdle()

        // Collect items from PagingData
        val collectedItems = mutableListOf<ImageEntity>()
        // Collect items in the PagingData
        result.map { item ->
            item.map {
                collectedItems.add(it)
            }

        }

        // Assert
        assertEquals(mockImages, collectedItems)
    }
}
