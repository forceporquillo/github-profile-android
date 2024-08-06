package dev.forcecodes.gitprofile.data.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class KeyIndex(
    @PrimaryKey
    val id: Int,
    val tag: String?,

    val prevSince: Int?,
    val nextSince: Int?
)
