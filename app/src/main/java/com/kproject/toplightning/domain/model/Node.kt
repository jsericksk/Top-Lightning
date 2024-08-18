package com.kproject.toplightning.domain.model

data class Node(
    val publicKey: String,
    val alias: String,
    val channels: Long,
    val capacity: Long,
    val firstSeen: Long,
    val updatedAt: Long,
    val city: TranslationCode?,
    val country: TranslationCode?,
    val isoCode: String?
)

data class TranslationCode(
    val de: String?,
    val en: String?,
    val es: String?,
    val fr: String?,
    val ja: String?,
    val ptBr: String?,
    val ru: String?,
    val zhCn: String?
)

val sampleNodeList = List(20) { index ->
    val country = TranslationCode(
        de = "Vereinigte Staaten",
        en = "United States",
        es = "Estados Unidos",
        fr = "États Unis",
        ja = "アメリカ",
        ptBr = "EUA",
        ru = "США",
        zhCn = "美国"
    )
    val city = TranslationCode(
        de = "New York City",
        en = "New York",
        es = "Nueva York",
        fr = "New York",
        ja = "ニューヨーク",
        ptBr = "Nova Iorque",
        ru = null,
        zhCn = null
    )
    Node(
        publicKey = "${index}9238484864ef025fde8fb587d9ak1k1186895ee44a926bfc370e2c3228ud203",
        alias = "BitBit",
        channels = 2547,
        capacity = 555000,
        firstSeen = 1522941222,
        updatedAt = 1723684919,
        city = city,
        country = country,
        isoCode = "US"
    )
}