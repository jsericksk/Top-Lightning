package com.kproject.toplightning.data.remote

import com.kproject.toplightning.data.remote.model.NodeResponse
import retrofit2.Response
import retrofit2.http.GET

interface MempoolApiService {

    @GET("/api/v1/lightning/nodes/rankings/connectivity")
    suspend fun getTopNodesByConnectivity(): Response<List<NodeResponse>>

    companion object {
        const val BASE_URL = "https://mempool.space/"
    }
}