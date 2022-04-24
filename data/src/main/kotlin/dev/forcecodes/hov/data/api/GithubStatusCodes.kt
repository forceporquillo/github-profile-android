package dev.forcecodes.hov.data.api

// TODO: Add more Github status codes responses.
sealed class GithubStatusCodes(
    open val message: String? = null,
    val status: Int,
) {
    data class Ok(
        override val message: String = "OK"
    ) : GithubStatusCodes(message, 200)

    // Rate limit exception
    data class Forbidden(
        override val message: String = "API rate limit exceeded"
    ): GithubStatusCodes(message,403)
}



