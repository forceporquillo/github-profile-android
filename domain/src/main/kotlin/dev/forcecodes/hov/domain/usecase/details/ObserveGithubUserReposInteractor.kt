package dev.forcecodes.hov.domain.usecase.details

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import dev.forcecodes.hov.data.api.models.RepositoryEntity
import dev.forcecodes.hov.data.cache.entity.OrganizationsEntity
import dev.forcecodes.hov.domain.source.DetailsRepository
import dev.forcecodes.hov.domain.usecase.PagingInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveGithubUserReposInteractor @Inject constructor(
    private val detailsRepository: DetailsRepository
) : PagingInteractor<ObserveGithubUserReposInteractor.Params, UserRepoUiModel, RepositoryEntity>() {

    override fun createObservable(params: Params): Flow<PagingData<UserRepoUiModel>> {
        return detailsRepository.getUserRepositories(params.name, params.pagingConfig).map {
            it.map { entity -> entity.toUiModel() }
        }.distinctUntilChanged()
    }

    override fun RepositoryEntity.toUiModel(id: String?): UserRepoUiModel {
        return UserRepoUiModel(
            id = this.id,
            name = name,
            starred = stargazersCount?.toString(),
            description = description,
            language = language
        )
    }

    data class Params(
        val name: String,
        override val pagingConfig: PagingConfig
    ) : PagingInteractor.Params<UserRepoUiModel>
}

data class UserRepoUiModel(
    val id: Int,
    val name: String?,
    val starred: String?,
    val description: String?,
    val language: String?
)

class ObserveGithubUserOrgsInteractor @Inject constructor(
    private val detailsRepository: DetailsRepository
) : PagingInteractor<ObserveGithubUserOrgsInteractor.Params, OrgsUiModel, OrganizationsEntity>() {

    override fun createObservable(params: Params): Flow<PagingData<OrgsUiModel>> {
        return detailsRepository.getUserOrganizations(params.name, params.id, params.pagingConfig)
            .map {
                it.map { entity -> entity.toUiModel() }
            }.distinctUntilChanged()
    }

    override fun OrganizationsEntity.toUiModel(id: String?): OrgsUiModel {
        return OrgsUiModel(
            id = this.id,
            description = description,
            name = login
        )
    }

    data class Params(
        val name: String,
        val id: Int,
        override val pagingConfig: PagingConfig
    ) : PagingInteractor.Params<OrgsUiModel>
}


data class OrgsUiModel(
    val id: Int,
    val description: String?,
    val name: String?,
)