package dev.forcecodes.gitprofile.data.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity @JvmOverloads constructor(

    val name: String? = null,

    val avatarUrl: String? = null,

    val githubUrl: String? = null,

    @PrimaryKey
    val id: Int
)