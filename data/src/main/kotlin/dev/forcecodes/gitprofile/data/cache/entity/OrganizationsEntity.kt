package dev.forcecodes.gitprofile.data.cache.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.forcecodes.gitprofile.data.api.models.OrganizationsResponse
import dev.forcecodes.gitprofile.data.api.models.Owner
import javax.inject.Inject

@Entity
data class OrganizationsEntity(

    @PrimaryKey
    val id: Int,

    val issuesUrl: String? = null,

    val reposUrl: String? = null,

    val avatarUrl: String? = null,

    val eventsUrl: String? = null,

    val membersUrl: String? = null,

    val description: String? = null,

    val hooksUrl: String? = null,

    val login: String? = null,

    val url: String? = null,

    val nodeId: String? = null,

    val publicMembersUrl: String? = null,

    @Embedded
    val owner: Owner? = null
)
