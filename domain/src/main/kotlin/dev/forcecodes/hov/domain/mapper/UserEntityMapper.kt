package dev.forcecodes.hov.domain.mapper

import dev.forcecodes.hov.core.Mapper
import dev.forcecodes.hov.data.api.models.UserResponse
import dev.forcecodes.hov.data.cache.entity.UserEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class UserEntityMapper @Inject constructor() : Mapper<List<UserResponse>, List<UserEntity>> {
    override fun invoke(data: List<UserResponse>): List<UserEntity> {
        return data.map { response ->
            UserEntity(
                name = response.login,
                avatarUrl = response.avatarUrl,
                githubUrl = response.htmlUrl,
                id = response.id ?: throw IllegalStateException()
            )
        }
    }
}