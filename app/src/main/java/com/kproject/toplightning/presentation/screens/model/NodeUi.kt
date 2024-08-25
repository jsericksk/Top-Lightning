package com.kproject.toplightning.presentation.screens.model

import com.kproject.toplightning.domain.model.Node
import com.kproject.toplightning.presentation.utils.DateUtils
import com.kproject.toplightning.presentation.utils.Utils
import java.util.Locale

data class NodeUi(
    val publicKey: String,
    val alias: String,
    val channels: String,
    val capacity: String,
    val firstSeen: Long,
    val updateDate: Long,
    val formattedFirstSeen: String,
    val formattedUpdateDate: String,
    val locality: Locality
)

data class Locality(
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
    val formattedFirstSeen = DateUtils.getFormattedDateTime(
        unixTime = node.firstSeen,
        dateTimePattern = "dd MMM yyyy"
    )
    val formattedUpdateDate = DateUtils.getRelativeTimeSpan(node.updatedAt)

    return NodeUi(
        publicKey = node.publicKey,
        alias = node.alias,
        channels = channels,
        capacity = capacity,
        firstSeen = node.firstSeen,
        updateDate = node.updatedAt,
        formattedFirstSeen = formattedFirstSeen,
        formattedUpdateDate = formattedUpdateDate,
        locality = node.getLocality()
    )
}

private fun Node.getLocality(): Locality {
    val languageMap = mapOf(
        "de" to Pair(country?.de, city?.de),
        "en" to Pair(country?.en, city?.en),
        "es" to Pair(country?.es, city?.es),
        "fr" to Pair(country?.fr, city?.fr),
        "ja" to Pair(country?.ja, city?.ja),
        "pt" to Pair(country?.ptBr, city?.ptBr),
        "ru" to Pair(country?.ru, city?.ru),
        "zh" to Pair(country?.zhCn, city?.zhCn)
    )

    /**
     * This only gets the current language from the device and not from the app. It may not be
     * reflected immediately if there is a language change while using the app.
     */
    val deviceLanguage = Locale.getDefault().language
    val (country, city) = languageMap[deviceLanguage] ?: Pair(country?.en, city?.en)

    return Locality(
        country = country,
        city = city,
        isoCode = isoCode
    )
}

val sampleLocality = Locality(
    country = "United States",
    city = "New York",
    isoCode = "US"
)

val sampleNodeList = List(20) { index ->
    val firstSeen = if (index % 2 == 0) 1522941222L else 1529506821L
    val formattedFirstSeen = DateUtils.getFormattedDateTime(
        unixTime = firstSeen,
        dateTimePattern = "dd MMM yyyy"
    )
    val currentTimeInUnix = System.currentTimeMillis() / 1000L
    val updateDate = currentTimeInUnix - ((index + 1) * 60)
    val formattedUpdateDate = DateUtils.getRelativeTimeSpan(updateDate)

    NodeUi(
        publicKey = "${index}9238484864ef025fde8fb587d9ak1k1186895ee44a926bfc370e2c3228ud203",
        alias = "BitBit",
        channels = "2547",
        capacity = "555.000 BTC",
        firstSeen = firstSeen,
        updateDate = updateDate,
        formattedFirstSeen = formattedFirstSeen,
        formattedUpdateDate = formattedUpdateDate,
        locality = sampleLocality,
    )
}