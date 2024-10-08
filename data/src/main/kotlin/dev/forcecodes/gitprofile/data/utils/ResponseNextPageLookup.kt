package dev.forcecodes.gitprofile.data.utils

import okhttp3.Headers

interface ResponseNextPageLookup {
    fun nextIndexFromHeader(headers: Headers?)
    fun nextPage(): Int
}