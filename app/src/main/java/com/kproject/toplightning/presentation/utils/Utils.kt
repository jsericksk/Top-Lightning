package com.kproject.toplightning.presentation.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import java.math.BigDecimal
import java.math.RoundingMode

object Utils {

    fun convertSatoshiToBitcoin(satoshis: String): String {
        return try {
            val satoshiValue = BigDecimal("100000000")
            val satoshisToConvert = BigDecimal(satoshis)
            val bitcoins = satoshisToConvert.divide(satoshiValue, 8, RoundingMode.HALF_EVEN)
            "$bitcoins BTC"
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