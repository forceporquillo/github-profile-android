package dev.forcecodes.gitprofile.data.api.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StarredResponse(

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "description")
    val description: String? = null,

    @PrimaryKey
    @Json(name = "id")
    val id: Int = 0,

    @Json(name = "stargazers_count")
    val stargazersCount: Int? = null,

    @Json(name = "language")
    val language: String? = null,

    @Embedded
    @Json(name = "owner")
    val owner: Owner? = null,
)

@Entity
data class StarredReposEntity(
    @PrimaryKey
    val id: Int = 0,

    val name: String? = null,
    val description: String? = null,

    val stargazersCount: Int? = null,
    val language: String? = null,

    @Embedded
    val owner: Owner? = null,

    @ColumnInfo(name = "starred_owner")
    val starredOwner: String? = null
)

