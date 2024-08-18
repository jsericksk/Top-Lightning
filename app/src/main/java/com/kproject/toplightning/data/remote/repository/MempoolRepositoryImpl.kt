package com.kproject.toplightning.data.remote.repository

import com.kproject.toplightning.commom.ResultState
import com.kproject.toplightning.data.remote.MempoolApiService
import com.kproject.toplightning.data.remote.model.toNodeModel
import com.kproject.toplightning.domain.model.Node
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MempoolRepositoryImpl(
    private val mempoolApiService: MempoolApiService
) : MempoolRepository {

    override suspend fun getTopNodesByConnectivity(): Flow<ResultState<List<Node>>> = flow {
        try {
            emit(ResultState.Loading)
            val response = mempoolApiService.getTopNodesByConnectivity()
            response.body()?.let { nodeResponseList ->
                val nodeList = nodeResponseList.map { nodeResponse -> nodeResponse.toNodeModel() }
                emit(ResultState.Success(nodeList))
            } ?: emit(ResultState.Error(Exception("Unknown error. Response body is null")))
        } catch (e: Exception) {
            emit(ResultState.Error(e))
        }
    }
}