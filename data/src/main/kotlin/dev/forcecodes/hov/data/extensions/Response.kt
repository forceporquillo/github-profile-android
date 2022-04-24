package dev.forcecodes.hov.data.extensions

import okhttp3.Response

internal val Response.containsNextPage: Boolean
    get() = request.url.encodedQuery?.contains("since") == true