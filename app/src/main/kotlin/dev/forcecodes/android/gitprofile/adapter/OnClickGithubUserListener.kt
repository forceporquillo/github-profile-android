package dev.forcecodes.android.gitprofile.adapter

fun interface OnClickGithubUserListener {
    fun onClick(info: Pair<Int, String>)
}