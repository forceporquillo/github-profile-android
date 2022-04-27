package dev.forcecodes.hov.domain.mapper

import dev.forcecodes.hov.core.Mapper
import dev.forcecodes.hov.core.model.DetailsUiModel
import dev.forcecodes.hov.core.model.empty
import dev.forcecodes.hov.data.cache.entity.UserDetailsEntity
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