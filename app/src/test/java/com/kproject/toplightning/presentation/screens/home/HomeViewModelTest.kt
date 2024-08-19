package com.kproject.toplightning.presentation.screens.home

import com.kproject.toplightning.data.local.repository.prefs.FakePreferencesRepository
import com.kproject.toplightning.data.remote.repository.FakeMempoolRepository
import com.kproject.toplightning.presentation.utils.ViewState
import com.kproject.toplightning.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: HomeViewModel
    private val preferenceRepository = FakePreferencesRepository()
    private val mempoolRepository = FakeMempoolRepository()

    @Before
    fun setup() {
        viewModel = HomeViewModel(
            mempoolRepository = mempoolRepository,
            preferenceRepository = preferenceRepository
        )
    }

    @Test
    fun uiState_whenInstantiatingViewModel_thenNodeListShouldBeLoaded() = runTest {
        val nodeList = uiState().nodeList
        assertTrue(nodeList.isNotEmpty())
        assertTrue(nodeList.size == 3)
    }

    @Test
    fun uiState_whenSortNodeList_thenShouldBeReloadedSorted() = runTest {
        val currentNodeList = uiState().nodeList
        assertEquals("ComposeCoin", currentNodeList[0].alias)
        assertEquals("BitMister", currentNodeList[1].alias)
        assertEquals("Coin Master", currentNodeList[2].alias)

        viewModel.onUiAction(HomeUiAction.ChangeListOrder(SortListBy.Capacity))
        var updatedNodeList = uiState().nodeList
        assertEquals("Coin Master", updatedNodeList[0].alias)
        assertEquals("BitMister", updatedNodeList[1].alias)
        assertEquals("ComposeCoin", updatedNodeList[2].alias)

        viewModel.onUiAction(HomeUiAction.ChangeListOrder(SortListBy.LastUpdate))
        updatedNodeList = uiState().nodeList
        assertEquals("BitMister", updatedNodeList[0].alias)
        assertEquals("ComposeCoin", updatedNodeList[1].alias)
        assertEquals("Coin Master", updatedNodeList[2].alias)

        viewModel.onUiAction(HomeUiAction.ChangeListOrder(SortListBy.Channels))
        updatedNodeList = uiState().nodeList
        assertEquals("ComposeCoin", updatedNodeList[0].alias)
        assertEquals("BitMister", updatedNodeList[1].alias)
        assertEquals("Coin Master", updatedNodeList[2].alias)
    }

    @Test
    fun uiState_whenRequestError_thenViewStateShouldBeError() = runTest {
        assertTrue(uiState().nodeList.isNotEmpty())

        mempoolRepository.simulateRequestError = true
        viewModel.onUiAction(HomeUiAction.RefreshList)
        assertTrue(uiState().viewState == ViewState.Error)
        assertTrue(uiState().nodeList.isEmpty())

        mempoolRepository.simulateRequestError = false
        viewModel.onUiAction(HomeUiAction.RefreshList)
        assertTrue(uiState().viewState == ViewState.Success)
        assertTrue(uiState().nodeList.isNotEmpty())
    }

    private fun uiState() = viewModel.uiState.value
}