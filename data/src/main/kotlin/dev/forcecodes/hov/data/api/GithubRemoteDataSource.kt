package dev.forcecodes.hov.data.api

import dev.forcecodes.hov.data.api.models.OrganizationsResponse
import dev.forcecodes.hov.data.api.models.RepositoryEntity
import dev.forcecodes.hov.data.api.models.UserDetailsResponse
import dev.forcecodes.hov.data.api.models.UserResponse
import dev.forcecodes.hov.data.api.models.StarredResponse
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

    suspend fun getOrganizations(
        name: String,
        page: Int,
        size: Int
    ): ApiResponse<List<OrganizationsResponse>> {
        return getResponse {
            githubApiService.getUserOrganizations(name, page, size)
        }
    }

    suspend fun getRepositories(
        name: String,
        page: Int,
        size: Int
    ): ApiResponse<List<RepositoryEntity>> {
        return getResponse {
            githubApiService.getUserRepositories(name, page, size)
        }
    }

    suspend fun getStarredRepositories(
        name: String,
        page: Int,
        size: Int
    ): ApiResponse<List<StarredResponse>> {
        return getResponse {
            githubApiService.getUserStarredRepos(name, page, size)
        }
    }
}
