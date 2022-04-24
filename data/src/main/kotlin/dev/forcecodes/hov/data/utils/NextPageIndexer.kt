package dev.forcecodes.hov.data.utils

interface NextPageIndexer {
    fun takeMaxAndStore(index: Int): Int
    fun clear()
}
