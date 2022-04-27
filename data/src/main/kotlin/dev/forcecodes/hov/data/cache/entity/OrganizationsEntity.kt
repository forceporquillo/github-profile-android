package dev.forcecodes.hov.data.cache.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.forcecodes.hov.data.api.models.OrganizationsResponse
import dev.forcecodes.hov.data.api.models.Owner
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

class OrganizationEntityMapper @Inject constructor() :
    EntityMapper<OrganizationsResponse, Owner, OrganizationsEntity> {

    override fun invoke(data: OrganizationsResponse, map: Owner): OrganizationsEntity {
        return data.run {
            OrganizationsEntity(
                id = id,
                issuesUrl = issuesUrl,
                reposUrl = reposUrl,
                avatarUrl = avatarUrl,
                eventsUrl = eventsUrl,
                membersUrl = membersUrl,
                description = description,
                hooksUrl = hooksUrl,
                login = login,
                url = url,
                nodeId = nodeId,
                publicMembersUrl = publicMembersUrl,
                owner = map
            )
        }
    }
}

interface EntityMapper<in T, in D, R> {
    operator fun invoke(data: T, map: D): R
}
