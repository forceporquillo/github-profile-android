package dev.forcecodes.hov.domain.usecase.details

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import dev.forcecodes.hov.data.cache.entity.OrganizationsEntity
import dev.forcecodes.hov.domain.source.DetailsRepository
import dev.forcecodes.hov.domain.usecase.PagingInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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