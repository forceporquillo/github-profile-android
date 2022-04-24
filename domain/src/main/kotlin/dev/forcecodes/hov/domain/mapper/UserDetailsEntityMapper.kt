package dev.forcecodes.hov.domain.mapper

import dev.forcecodes.hov.core.Mapper
import dev.forcecodes.hov.data.api.models.UserDetailsResponse
import dev.forcecodes.hov.data.cache.entity.UserDetailsEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDetailsEntityMapper @Inject constructor() :
    Mapper<UserDetailsResponse, UserDetailsEntity> {

    override fun invoke(data: UserDetailsResponse): UserDetailsEntity {
        return data.run {
            UserDetailsEntity(
                id = id ?: throw IllegalStateException("Id cannot be null"),
                name = login,
                displayName = name,
                location = location,
                company = company,
                email = email,
                twitter = twitterUsername,
                bio = bio,
                followers = followers ?: 0,
                following = following ?: 0
            )
        }
    }

}