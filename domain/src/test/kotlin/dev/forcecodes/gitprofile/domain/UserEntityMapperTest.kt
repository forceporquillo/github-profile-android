package dev.forcecodes.gitprofile.domain

import dev.forcecodes.gitprofile.data.api.models.UserResponse
import dev.forcecodes.gitprofile.data.cache.entity.UserEntity
import dev.forcecodes.gitprofile.domain.mapper.UserEntityMapper
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UserEntityMapperTest {

    private lateinit var userEntityMapper: UserEntityMapper

    @Before
    fun before() {
        userEntityMapper = UserEntityMapper()
    }

    @Test
    fun testEntity() {
        val listOfUsers = listOf(
            UserResponse(
                htmlUrl = "http://github.con/JohnDoe",
                avatarUrl = "https://avatars.githubusercontent.com/u/584?v=4",
                login = "JohnDoe",
                id = 1,
            ),
            UserResponse(
                htmlUrl = "http://github.con/JaneDoe",
                avatarUrl = "https://avatars.githubusercontent.com/u/584?v=4",
                login = "JaneDoe",
                id = 2
            )
        )

        val expected = listOf(
            UserEntity(
                githubUrl = "http://github.con/JohnDoe",
                avatarUrl = "https://avatars.githubusercontent.com/u/584?v=4",
                name = "JohnDoe",
                id = 1,
            ),
            UserEntity(
                githubUrl = "http://github.con/JaneDoe",
                avatarUrl = "https://avatars.githubusercontent.com/u/584?v=4",
                name = "JaneDoe",
                id = 2,
            )
        )

        Assert.assertEquals(expected,  userEntityMapper.invoke(listOfUsers))
    }
}