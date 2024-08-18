package com.kproject.toplightning.data

import com.kproject.toplightning.domain.model.Node
import com.kproject.toplightning.domain.model.TranslationCode

private val country = TranslationCode(
    de = "Vereinigte Staaten",
    en = "United States",
    es = "Estados Unidos",
    fr = "États Unis",
    ja = "アメリカ",
    ptBr = "EUA",
    ru = "США",
    zhCn = "美国"
)

private val city = TranslationCode(
    de = "New York City",
    en = "New York",
    es = "Nueva York",
    fr = "New York",
    ja = "ニューヨーク",
    ptBr = "Nova Iorque",
    ru = null,
    zhCn = null
)

val fakeNodeList = listOf(
    Node(
        publicKey = "9238484864ef025fde8fb587d9ak1k1186895ee44a926bfc370e2c3228ud203",
        alias = "ComposeCoin",
        channels = 2547,
        capacity = 100000,
        firstSeen = 1522941222,
        updatedAt = 1723939564,
        city = city,
        country = country,
        isoCode = "US"
    ),

    Node(
        publicKey = "1238484864ef025fde8fb587d9ak1k1186895ee44a926bfc370e2c3228ud203",
        alias = "BitMister",
        channels = 1500,
        capacity = 300000,
        firstSeen = 1593656979,
        updatedAt = 1723940217,
        city = city,
        country = country,
        isoCode = "US"
    ),

    Node(
        publicKey = "4568484864ef025fde8fb587d9ak1k1186895ee44a926bfc370e2c3228ud203",
        alias = "Coin Master",
        channels = 850,
        capacity = 500000,
        firstSeen = 1552664640,
        updatedAt = 1723938885,
        city = city,
        country = country,
        isoCode = "US"
    )
)