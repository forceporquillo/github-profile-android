package dev.forcecodes.gitprofile.data.api

import dev.forcecodes.gitprofile.data.api.models.OrganizationsResponse
import dev.forcecodes.gitprofile.data.api.models.RepositoryEntity
import dev.forcecodes.gitprofile.data.api.models.UserDetailsResponse
import dev.forcecodes.gitprofile.data.api.models.UserResponse
import dev.forcecodes.gitprofile.data.api.models.StarredResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {

    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Int,
        @Query("per_page") pageSize: Int = 30
    ): Response<List<UserResponse>>

    @GET("users/{name}")
    suspend fun getDetails(
        @Path("name") name: String
    ): Response<UserDetailsResponse>

    @GET("users/{name}/orgs")
    suspend fun getUserOrganizations(
        @Path("name") name: String,
        @Query("page") page: Int,
        @Query("per_page") size: Int
    ): Response<List<OrganizationsResponse>>

    @GET("users/{name}/repos")
    suspend fun getUserRepositories(
        @Path("name") name: String,
        @Query("page") page: Int,
        @Query("per_page") size: Int
    ): Response<List<RepositoryEntity>>

    @GET("users/{name}/starred")
    suspend fun getUserStarredRepos(
        @Path("name") name: String,
        @Query("page") page: Int,
        @Query("per_page") size: Int
    ): Response<List<StarredResponse>>
}