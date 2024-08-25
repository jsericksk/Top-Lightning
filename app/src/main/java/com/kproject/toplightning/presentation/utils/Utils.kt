package com.kproject.toplightning.presentation.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat

object Utils {

    fun convertSatoshiToBitcoin(satoshis: String): String {
        return try {
            val satoshiValue = BigDecimal("100000000")
            val satoshisToConvert = BigDecimal(satoshis)
            val bitcoins = satoshisToConvert.divide(satoshiValue, 8, RoundingMode.HALF_EVEN)
            val numberFormat = NumberFormat.getInstance()
            numberFormat.minimumFractionDigits = 8
            numberFormat.maximumFractionDigits = 8
            val formattedBitcoins = numberFormat.format(bitcoins)
            "$formattedBitcoins BTC"
        } catch (e: Exception) {
            "?.???????? BTC"
        }
    }

    fun copyToClipBoard(context: Context, text: String) {
        val clipBoard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("", text)
        clipBoard.setPrimaryClip(clipData)
    }
}