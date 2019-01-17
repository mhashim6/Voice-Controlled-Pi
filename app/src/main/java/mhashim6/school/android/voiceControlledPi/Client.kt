package mhashim6.school.android.voiceControlledPi

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

const val url = "http://192.168.43.182:8585/"

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
    "ten" to "10"
)

private val COMMANDS = listOf("light", "dark", "shutdown")
private val COLORS = listOf("red", "green", "yellow")
private val client = OkHttpClient()

enum class Response { SUCCESSFUL, ERR_WRONG_COMMAND, ERR_CONNECTION }

/** Basic input sanitization.*/
suspend fun processCommand(voiceCmd: String): Response {
    var response = Response.SUCCESSFUL
    val ledNumber = LED_NUMBERS[voiceCmd]

    try {
        when {
            ledNumber != null -> toggle(led = ledNumber)
            COLORS.contains(voiceCmd) -> toggleColor(voiceCmd)
            COMMANDS.contains(voiceCmd) -> sendCommand(voiceCmd)
            else -> response = Response.ERR_WRONG_COMMAND
        }
    } catch (e: Exception) {
        Log.e("oh God why!!!", e.toString())
        response = Response.ERR_CONNECTION
    } finally {
        return response
    }
}

private suspend fun toggle(led: String) = withContext(Dispatchers.IO) {
    val request = Request.Builder()
        .url(url + "toggle/$led")
        .put(RequestBody.create(null, ""))
        .build()
    client.newCall(request).execute()
}

private suspend fun toggleColor(color: String) = withContext(Dispatchers.IO) {
    val request = Request.Builder()
        .url(url + "toggle/color/$color")
        .put(RequestBody.create(null, ""))
        .build()
    client.newCall(request).execute()
}

private suspend fun sendCommand(command: String) = withContext(Dispatchers.IO) {
    val request = Request.Builder()
        .url(url + command)
        .put(RequestBody.create(null, ""))
        .build()
    client.newCall(request).execute()
}