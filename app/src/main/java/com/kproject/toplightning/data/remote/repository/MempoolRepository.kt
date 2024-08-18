package com.kproject.toplightning.data.remote.repository

import com.kproject.toplightning.commom.ResultState
import com.kproject.toplightning.domain.model.Node
import kotlinx.coroutines.flow.Flow

interface MempoolRepository {
    suspend fun getTopNodesByConnectivity(): Flow<ResultState<List<Node>>>
}