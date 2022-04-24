package dev.forcecodes.hov.data.api

import dev.forcecodes.hov.data.api.models.UserDetailsResponse
import dev.forcecodes.hov.data.api.models.UserResponse
import dev.forcecodes.hov.data.internal.GithubApi
import javax.inject.Inject

class GithubRemoteDataSource @Inject constructor(
    @GithubApi private val githubApiService: GithubApiService
) {

    suspend fun getUsers(
        since: Int,
        maxRefreshSize: Int
    ): ApiResponse<List<UserResponse>> {
        return getResponse {
            githubApiService
                .getUsers(since, maxRefreshSize)
        }

    }

    suspend fun getDetails(
        name: String
    ): ApiResponse<UserDetailsResponse> {
        return getResponse {
            githubApiService.getDetails(name)
        }
    }
}
