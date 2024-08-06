package dev.forcecodes.gitprofile.core.model

data class DetailsUiModel(
    override val id: Int,
    override val name: String,
    override val displayName: String,
    override val location: String,
    override val company: String,
    override val email: String,
    override val twitter: String,
    override val bio: String,
    override val followers: Int,
    override val following: Int,
    val followersString: String,
    val followingString: String
) : Details

fun String?.empty(): String {
    return this ?: ""
}

