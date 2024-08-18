package com.kproject.toplightning.data.remote.repository

import com.kproject.toplightning.commom.ResultState
import com.kproject.toplightning.data.fakeNodeList
import com.kproject.toplightning.domain.model.Node
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMempoolRepository : MempoolRepository {

    override suspend fun getTopNodesByConnectivity(): Flow<ResultState<List<Node>>> = flow {
        emit(ResultState.Loading)
        emit(ResultState.Success(fakeNodeList))
    }
}