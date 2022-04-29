package dev.forcecodes.hov.domain.mapper

import dev.forcecodes.hov.core.EntityMapper
import dev.forcecodes.hov.data.api.models.OrganizationsResponse
import dev.forcecodes.hov.data.api.models.Owner
import dev.forcecodes.hov.data.api.models.StarredReposEntity
import dev.forcecodes.hov.data.api.models.StarredResponse
import dev.forcecodes.hov.data.cache.entity.OrganizationsEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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

@Singleton
class StarredReposEntityEntityMapper @Inject constructor() :
    EntityMapper<StarredResponse, String, StarredReposEntity> {

    override fun invoke(data: StarredResponse, map: String): StarredReposEntity {
        return data.run {
            StarredReposEntity(
                id = id,
                name = name,
                description = description,
                language = language,
                stargazersCount = stargazersCount,
                starredOwner = map,
                owner = owner
            )
        }
    }
}