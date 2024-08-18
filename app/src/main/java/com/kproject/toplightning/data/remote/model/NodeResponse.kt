package com.kproject.toplightning.data.remote.model

import com.google.gson.annotations.SerializedName
import com.kproject.toplightning.domain.model.Node
import com.kproject.toplightning.domain.model.TranslationCode

data class NodeResponse(
    val publicKey: String,
    val alias: String,
    val channels: Long,
    val capacity: Long,
    val firstSeen: Long,
    val updatedAt: Long,
    val city: TranslationCodeResponse?,
    val country: TranslationCodeResponse?,
    @SerializedName("iso_code") val isoCode: String?
)

data class TranslationCodeResponse(
    val de: String?,
    val en: String?,
    val es: String?,
    val fr: String?,
    val ja: String?,
    @SerializedName("pt-BR") val ptBr: String?,
    val ru: String?,
    @SerializedName("zh-CN") val zhCn: String?
)

fun NodeResponse.toNodeModel(): Node = Node(
    publicKey = publicKey,
    alias = alias,
    channels = channels,
    capacity = capacity,
    firstSeen = firstSeen,
    updatedAt = updatedAt,
    city = city?.toTranslationCodeModel(),
    country = country?.toTranslationCodeModel(),
    isoCode = isoCode
)

fun TranslationCodeResponse.toTranslationCodeModel(): TranslationCode = TranslationCode(
    de = de,
    en = en,
    es = es,
    fr = fr,
    ja = ja,
    ptBr = ptBr,
    ru = ru,
    zhCn = zhCn
)