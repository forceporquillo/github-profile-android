package dev.forcecodes.hov.data.api

import dev.forcecodes.hov.data.api.models.UserDetailsResponse
import dev.forcecodes.hov.data.api.models.UserResponse
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
}