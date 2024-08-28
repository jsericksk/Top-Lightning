package com.kproject.toplightning.presentation.screens.model

import com.kproject.toplightning.domain.model.Node
import com.kproject.toplightning.presentation.utils.DateUtils
import com.kproject.toplightning.presentation.utils.Utils
import java.text.NumberFormat
import java.util.Locale

data class NodeUi(
    val publicKey: String,
    val alias: String,
    val channels: Long,
    val capacity: Long,
    val firstSeen: Long,
    val updateDate: Long,
    val locality: Locality
) {
    val formattedChannels: String by lazy { NumberFormat.getInstance().format(channels) }

    val formattedCapacity: String by lazy { Utils.convertSatoshiToBitcoin(capacity.toString()) }

    val formattedFirstSeen: String by lazy {
        DateUtils.getFormattedDateTime(
            unixTime = firstSeen,
            dateTimePattern = "dd MMM yyyy"
        )
    }

    val formattedUpdateDate: String by lazy { DateUtils.getRelativeTimeSpan(updateDate) }
}

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
    return NodeUi(
        publicKey = node.publicKey,
        alias = node.alias,
        channels = node.channels,
        capacity = node.capacity,
        firstSeen = node.firstSeen,
        updateDate = node.updatedAt,
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
    val alias = if (index % 2 == 0) "BitBit" else "JokBit Hujam Hamam Kajan"
    val firstSeen = if (index % 2 == 0) 1522941222L else 1529506821L
    val currentTimeInUnix = System.currentTimeMillis() / 1000L
    val updateDate = currentTimeInUnix - ((index + 1) * 60)

    NodeUi(
        publicKey = "${index}9238484864ef025fde8fb587d9ak1k1186895ee44a926bfc370e2c3228ud203",
        alias = alias,
        channels = 2547,
        capacity = 550000,
        firstSeen = firstSeen,
        updateDate = updateDate,
        locality = sampleLocality
    )
}