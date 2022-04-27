package dev.forcecodes.hov.data.api.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class RepositoryEntity(

    @Json(name = "stargazers_count")
    val stargazersCount: Int? = null,

    @Json(name = "html_url")
    val htmlUrl: String? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "description")
    val description: String? = null,

    @Json(name = "created_at")
    val createdAt: String? = null,

    @Json(name = "language")
    val language: String? = null,

    @Json(name = "default_branch")
    val defaultBranch: String? = null,

    @PrimaryKey
    @Json(name = "id")
    val id: Int = 0,

    @Json(name = "watchers_count")
    val watchersCount: Int? = null,

    @Json(name = "node_id")
    val nodeId: String? = null,

    @Json(name = "owner")
    @Embedded
    val owner: Owner? = null
)


@JsonClass(generateAdapter = true)
data class Owner(
    @ColumnInfo(name="owner_login")
    val login: String?,

    @ColumnInfo(name="owner_id")
    val id: String?
)