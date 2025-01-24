package org.ton.net

import java.net.HttpURLConnection
import java.net.URI

const val TONCENTER_API_V3 = "https://toncenter.com/api/v3"

fun String.toUrlAddress() = replace(":", "%3A")

fun makeRequest(query: String, failOnRequestError: Boolean = true): Pair<Int, String> {
    val connection = URI(query).toURL().openConnection() as? HttpURLConnection
        ?: error("Could not cast connection to HttpURLConnection")
    val responseCode = connection.responseCode
    if (!failOnRequestError && responseCode !in 200..<300) {
        return responseCode to ""
    }
    check(responseCode in 200..<300) {
        "Request $query returned response code $responseCode"
    }
    return responseCode to connection.inputStream.readBytes().decodeToString()
}
