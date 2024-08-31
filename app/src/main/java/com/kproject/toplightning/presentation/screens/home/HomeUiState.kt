package com.kproject.toplightning.presentation.screens.home

import com.kproject.toplightning.R
import com.kproject.toplightning.domain.model.Node
import com.kproject.toplightning.presentation.screens.model.NodeUi
import com.kproject.toplightning.presentation.utils.ViewState

data class HomeUiState(
    val viewState: ViewState = ViewState.Loading,
    val isDarkMode: Boolean = true,
    val nodeList: List<NodeUi> = emptyList(),
    val searchedNodeList: List<NodeUi> = emptyList(),
    val isRefreshingNodeList: Boolean = false,
    val sortListBy: SortListBy = SortListBy.Channels,
    val searchQuery: String = "",
) {
    val isSuccess = viewState == ViewState.Success
    val realNodeList = if (searchQuery.isNotBlank()) searchedNodeList else nodeList
}

sealed class HomeUiAction {
    data object ChangeAppTheme : HomeUiAction()
    data object RefreshList : HomeUiAction()
    data class ChangeListOrder(val sortListBy: SortListBy) : HomeUiAction()
    data class ChangeSearchQuery(val searchQuery: String) : HomeUiAction()
}

enum class SortListBy(val titleResId: Int) {
    Channels(R.string.sort_list_by_channels),
    Capacity(R.string.sort_list_by_capacity),
    LastUpdate(R.string.sort_list_by_last_update),
}