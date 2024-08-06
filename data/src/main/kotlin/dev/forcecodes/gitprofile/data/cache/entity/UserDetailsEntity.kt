package dev.forcecodes.gitprofile.data.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.forcecodes.gitprofile.core.model.Details

@Entity
data class UserDetailsEntity(
    @PrimaryKey
    override val id: Int = 0,
    override val name: String? = null,
    override val displayName: String? = null,
    override val location: String? = null,
    override val company: String? = null,
    override val email: String? = null,
    override val twitter: String? = null,
    override val bio: String? = null,
    override val followers: Int = 0,
    override val following: Int = 0
) : Details