package com.kproject.toplightning.presentation.screens.home

import com.kproject.toplightning.data.local.repository.prefs.FakePreferencesRepository
import com.kproject.toplightning.data.remote.repository.FakeMempoolRepository
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
        assertEquals("ComposeCoin", uiState().nodeList[0].alias)
        assertEquals("BitMister", uiState().nodeList[1].alias)
        assertEquals("Coin Master", uiState().nodeList[2].alias)

        viewModel.onUiAction(HomeUiAction.ChangeListOrder(SortListBy.Capacity))
        assertEquals("Coin Master", uiState().nodeList[0].alias)
        assertEquals("BitMister", uiState().nodeList[1].alias)
        assertEquals("ComposeCoin", uiState().nodeList[2].alias)

        viewModel.onUiAction(HomeUiAction.ChangeListOrder(SortListBy.LastUpdate))
        assertEquals("BitMister", uiState().nodeList[0].alias)
        assertEquals("ComposeCoin", uiState().nodeList[1].alias)
        assertEquals("Coin Master", uiState().nodeList[2].alias)

        viewModel.onUiAction(HomeUiAction.ChangeListOrder(SortListBy.Channels))
        assertEquals("ComposeCoin", uiState().nodeList[0].alias)
        assertEquals("BitMister", uiState().nodeList[1].alias)
        assertEquals("Coin Master", uiState().nodeList[2].alias)
    }

    private fun uiState() = viewModel.uiState.value
}