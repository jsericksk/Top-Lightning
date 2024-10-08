package com.kproject.toplightning.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kproject.toplightning.commom.ResultState
import com.kproject.toplightning.commom.constants.PrefsConstants
import com.kproject.toplightning.data.local.repository.prefs.PreferenceRepository
import com.kproject.toplightning.data.remote.repository.MempoolRepository
import com.kproject.toplightning.presentation.screens.model.NodeUi
import com.kproject.toplightning.presentation.screens.model.toNodeUiList
import com.kproject.toplightning.presentation.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mempoolRepository: MempoolRepository,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> get() = _uiState

    init {
        getCurrentTheme()
        getTopNodesByConnectivity()
    }

    fun onUiAction(action: HomeUiAction) {
        when (action) {
            is HomeUiAction.ChangeAppTheme -> {
                changeTheme()
            }

            is HomeUiAction.RefreshList -> {
                _uiState.update { it.copy(isRefreshingNodeList = true) }
                getTopNodesByConnectivity()
            }

            is HomeUiAction.ChangeListOrder -> {
                sortNodeList(action.sortListBy)
            }

            is HomeUiAction.ChangeSearchQuery -> {
                _uiState.update { it.copy(searchQuery = action.searchQuery) }
                searchNodes(action.searchQuery)
            }
        }
    }

    private fun searchNodes(nodeAlias: String) {
        val nodeList = uiState.value.nodeList
        val searchedList = nodeList.filter { nodeUi ->
            nodeUi.alias.lowercase().contains(nodeAlias.lowercase())
        }
        _uiState.update { it.copy(searchedNodeList = searchedList) }
    }

    private fun getTopNodesByConnectivity() {
        viewModelScope.launch {
            mempoolRepository.getTopNodesByConnectivity().collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _uiState.update {
                            it.copy(
                                viewState = ViewState.Loading,
                                nodeList = emptyList()
                            )
                        }
                    }

                    is ResultState.Success -> {
                        val nodeList = result.data.toNodeUiList()
                        val currentListSortBy = uiState.value.sortListBy
                        val sortedNodeList = if (currentListSortBy != SortListBy.Channels) {
                            nodeList.getSortedNodeList(sortListBy = currentListSortBy)
                        } else {
                            // The list is already sorted by channels by the API,
                            // so there is no need to reorder it.
                            nodeList
                        }
                        _uiState.update {
                            it.copy(
                                viewState = ViewState.Success,
                                nodeList = sortedNodeList,
                                isRefreshingNodeList = false
                            )
                        }
                    }

                    is ResultState.Error -> {
                        _uiState.update {
                            it.copy(
                                viewState = ViewState.Error,
                                nodeList = emptyList(),
                                isRefreshingNodeList = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun sortNodeList(sortListBy: SortListBy) {
        if (sortListBy == uiState.value.sortListBy) return
        val currentList = uiState.value.nodeList
        _uiState.update {
            it.copy(
                sortListBy = sortListBy,
                nodeList = currentList.getSortedNodeList(sortListBy = sortListBy)
            )
        }
    }

    private fun List<NodeUi>.getSortedNodeList(sortListBy: SortListBy): List<NodeUi> {
        val nodeList = this
        val updatedNodeList = when (sortListBy) {
            SortListBy.Channels -> {
                nodeList.sortedByDescending { it.channels }
            }

            SortListBy.Capacity -> {
                nodeList.sortedByDescending { it.capacity }
            }

            SortListBy.LastUpdate -> {
                nodeList.sortedByDescending { it.updateDate }
            }
        }
        return updatedNodeList
    }

    private fun getCurrentTheme() {
        val isDarkTheme = preferenceRepository.getPreference(
            key = PrefsConstants.DARK_MODE,
            defaultValue = true
        )
        _uiState.update { it.copy(isDarkMode = isDarkTheme) }
    }

    private fun changeTheme() {
        val newTheme = !uiState.value.isDarkMode
        preferenceRepository.savePreference(
            key = PrefsConstants.DARK_MODE,
            value = newTheme
        )
        _uiState.update { it.copy(isDarkMode = newTheme) }
    }
}