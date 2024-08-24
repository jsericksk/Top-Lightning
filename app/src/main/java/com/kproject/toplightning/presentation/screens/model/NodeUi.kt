package com.kproject.toplightning.presentation.screens.model

import com.kproject.toplightning.domain.model.Node
import com.kproject.toplightning.domain.model.TranslationCode
import com.kproject.toplightning.presentation.utils.Utils

data class NodeUi(
    val publicKey: String,
    val alias: String,
    val channels: String,
    val capacity: String,
    val firstSeen: Long,
    val updateDate: Long,
    val formattedFirstSeen: String,
    val formattedUpdateDate: String,
    val country: String?,
    val city: String?,
    val isoCode: String?
)

fun List<Node>.toNodeUiList(): List<NodeUi> {
    return this.map { it.toNodeUi() }
}

fun Node.toNodeUi(): NodeUi {
    val node = this
    val channels = node.channels.toString()
    val capacity = Utils.convertSatoshiToBitcoin(node.capacity.toString())
    val formattedFirstSeen = Utils.formatDate(node.firstSeen)
    val formattedUpdateDate = Utils.formatDate(node.updatedAt)

    val country = node.country?.ptBr ?: node.country?.en
    val city = node.city?.ptBr ?: node.city?.en

    return NodeUi(
        publicKey = node.publicKey,
        alias = node.alias,
        channels = channels,
        capacity = capacity,
        firstSeen = node.firstSeen,
        updateDate = node.updatedAt,
        formattedFirstSeen = formattedFirstSeen,
        formattedUpdateDate = formattedUpdateDate,
        country = country,
        city = city,
        isoCode = node.isoCode
    )
}

val sampleNodeList = List(20) { index ->
    NodeUi(
        publicKey = "${index}9238484864ef025fde8fb587d9ak1k1186895ee44a926bfc370e2c3228ud203",
        alias = "BitBit",
        channels = "2547",
        capacity = "555.000 BTC",
        firstSeen = 1522941222,
        updateDate = 1723684919,
        formattedUpdateDate = "",
        formattedFirstSeen = "",
        country = "United States",
        city = "New York",
        isoCode = "US"
    )
}