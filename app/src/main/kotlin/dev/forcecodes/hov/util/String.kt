package dev.forcecodes.hov.util

private const val ID = "id"
private const val IMAGE_URI = "https://avatars.githubusercontent.com/u/$ID?v=4"

val String.toUserContentUri: String
    get() = IMAGE_URI.replace(ID, this)

fun CharSequence?.notNull(): Boolean = this != null