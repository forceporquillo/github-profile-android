package dev.forcecodes.hov.data.api.models

import androidx.room.Embedded
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrganizationsResponse(

    @Json(name = "issues_url")
    val issuesUrl: String? = null,

    @Json(name = "repos_url")
    val reposUrl: String? = null,

    @Json(name = "avatar_url")
    val avatarUrl: String? = null,

    @Json(name = "events_url")
    val eventsUrl: String? = null,

    @Json(name = "members_url")
    val membersUrl: String? = null,

    @Json(name = "description")
    val description: String? = null,

    @PrimaryKey
    @Json(name = "id")
    val id: Int,

    @Json(name = "hooks_url")
    val hooksUrl: String? = null,

    @Json(name = "login")
    val login: String? = null,

    @Json(name = "url")
    val url: String? = null,

    @Json(name = "node_id")
    val nodeId: String? = null,

    @Json(name = "public_members_url")
    val publicMembersUrl: String? = null
)
