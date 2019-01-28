package mhashim6.school.android.voiceControlledPi

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

const val URL = "http://192.168.1.9:8585/" // 43.182

private val LED_NUMBERS = mapOf(
    "1" to "1",
    "2" to "2",
    "3" to "3",
    "4" to "4",
    "5" to "5",
    "6" to "6",
    "7" to "7",
    "8" to "8",
    "9" to "9",
    "10" to "10",
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9",
    "ten" to "10",
    "واحد" to "1",
    "اثنين" to "2",
    "ثلاثه" to "3",
    "اربعه" to "4",
    "خمسه" to "5",
    "سته" to "6",
    "سبعه" to "7",
    "ثمانيه" to "8",
    "تسعه" to "9",
    "عشره" to "10"
)

private val VOCAB = mapOf(
    "light" to "light",
    "dark" to "dark",
    "stop" to "dark",
    "off" to "dark",
    "dance" to "dance",
    "animation" to "dance",
    "thanks" to "dance",
    "نور" to "light",
    "ظلمه" to "dark",
    "اطفي" to "dark",
    "تحرك" to "dance",
    "اتحرك" to "dance",
    "انيميشن" to "dance",
    "شكرا" to "dance",
    "光" to "light",
    "闇" to "dark",
    "ダンス" to "dance",
    "ありがとうございます" to "dance"
)

private val COLORS = mapOf(
    "red" to "red",
    "green" to "green",
    "yellow" to "yellow",
    "احمر" to "red",
    "اخضر" to "green",
    "اصفر" to "yellow",
    "赤" to "red",
    "緑" to "green",
    "黄" to "yellow"
)

private val client = OkHttpClient()

enum class Response { SUCCESSFUL, ERR_WRONG_COMMAND, ERR_CONNECTION }

/** Basic input sanitization.*/
fun processCommand(voiceCmd: String): Response {
    var response = Response.SUCCESSFUL

    try {
        when {
            LED_NUMBERS.contains(voiceCmd) -> sendCommand("toggle/${LED_NUMBERS[voiceCmd]}")
            COLORS.contains(voiceCmd) -> sendCommand("toggle/color/${COLORS[voiceCmd]}")
            VOCAB.contains(voiceCmd) -> sendCommand(VOCAB[voiceCmd]!!)
            else -> response = Response.ERR_WRONG_COMMAND
        }
    } catch (e: Exception) {
        Log.e("oh God why!!!", e.toString())
        response = Response.ERR_CONNECTION
    } finally {
        return response
    }
}

private fun sendCommand(endpoint: String) {
    val request = Request.Builder()
        .url(URL + endpoint)
        .put(RequestBody.create(null, ""))
        .build()
    client.newCall(request).execute()
}