package com.kproject.toplightning.domain.model

data class Node(
    val publicKey: String,
    val alias: String,
    val channels: Long,
    val capacity: Long,
    val firstSeen: Long,
    val updatedAt: Long,
    val country: TranslationCode?,
    val city: TranslationCode?,
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