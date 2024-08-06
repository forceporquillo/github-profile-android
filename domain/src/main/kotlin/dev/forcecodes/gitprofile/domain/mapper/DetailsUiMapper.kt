package dev.forcecodes.gitprofile.domain.mapper

import dev.forcecodes.gitprofile.data.cache.entity.UserDetailsEntity
import dev.forcecodes.gitprofile.core.Mapper
import dev.forcecodes.gitprofile.core.model.DetailsUiModel
import dev.forcecodes.gitprofile.core.model.empty
import java.text.NumberFormat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsUiMapper @Inject constructor() : Mapper<UserDetailsEntity?, DetailsUiModel?> {

    private val numberFormatter = NumberFormat.getNumberInstance()

    override fun invoke(data: UserDetailsEntity?): DetailsUiModel? {
        return data?.run {
            val twitterHandle = if (twitter.isNullOrEmpty()) twitter.empty() else "@$twitter"
            DetailsUiModel(
                id,
                name.empty(),
                displayName.empty(),
                location.empty(),
                company.empty(),
                email.empty(),
                twitterHandle,
                bio.empty(),
                0, 0,
                numberFormatter.format(followers).empty(),
                numberFormatter.format(following).empty()
            )
        }
    }
}