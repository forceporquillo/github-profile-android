package dev.forcecodes.gitprofile.core.model

interface Details {
    val id: Int
    val name: String?
    val displayName: String?
    val location: String?
    val company: String?
    val email: String?
    val twitter: String?
    val bio: String?
    val followers: Int
    val following: Int
}