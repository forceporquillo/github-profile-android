package dev.forcecodes.hov.domain.usecase.details

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import dev.forcecodes.hov.core.model.empty
import dev.forcecodes.hov.data.api.models.RepositoryEntity
import dev.forcecodes.hov.domain.source.DetailsRepository
import dev.forcecodes.hov.domain.usecase.PagingInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.json.JSONException
import org.json.JSONObject
import java.text.NumberFormat
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

private const val FALLBACK_COLOR = "#00ffffff"

@Singleton
class ObserveGithubUserReposInteractor @Inject constructor(
    @Named("jsonColor")
    private val jsonObject: JSONObject,
    private val detailsRepository: DetailsRepository
) : PagingInteractor<ObserveGithubUserReposInteractor.Params, UserRepoUiModel, RepositoryEntity>() {

    private val numberFormatter = NumberFormat.getNumberInstance()

    override fun createObservable(params: Params): Flow<PagingData<UserRepoUiModel>> {
        return detailsRepository.getUserRepositories(params.name, params.pagingConfig).map {
            it.map { entity -> entity.toUiModel() }
        }.distinctUntilChanged()
    }

    override fun RepositoryEntity.toUiModel(id: String?): UserRepoUiModel {
        return UserRepoUiModel(
            id = this.id,
            name = name,
            starred = formatStarredCount(stargazersCount),
            description = description,
            language = language,
            color = getColor(language)
        )
    }

    private fun formatStarredCount(stargazersCount: Int?): String {
        return numberFormatter.format(stargazersCount ?: 0).empty()
    }

    private fun getColor(language: String?): String {
        return try {
            if (language.isNullOrEmpty()) {
                throw JSONException("No Color Value Default to $FALLBACK_COLOR")
            }
            jsonObject.getString(language)
        } catch (e: JSONException) {
            FALLBACK_COLOR
        }
    }

    data class Params(
        val name: String,
        override val pagingConfig: PagingConfig
    ) : PagingInteractor.Params<UserRepoUiModel>
}

data class UserRepoUiModel(
    val id: Int,
    val name: String?,
    val starred: String,
    val description: String?,
    val language: String?,
    val color: String?
)