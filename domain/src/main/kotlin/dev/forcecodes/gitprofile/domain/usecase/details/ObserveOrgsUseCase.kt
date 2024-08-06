package dev.forcecodes.gitprofile.domain.usecase.details

import dev.forcecodes.gitprofile.core.Result
import dev.forcecodes.gitprofile.core.internal.Logger
import dev.forcecodes.gitprofile.core.qualifiers.IoDispatcher
import dev.forcecodes.gitprofile.core.successOr
import dev.forcecodes.gitprofile.domain.source.DetailsRepository
import dev.forcecodes.gitprofile.domain.usecase.BaseFlowUseCase
import dev.forcecodes.gitprofile.domain.usecase.UseCaseParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveOrgsUseCase @Inject constructor(
    private val detailsRepository: DetailsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseFlowUseCase<ObserveOrgsUseCase.Params, List<OrgsUiModel>>(dispatcher) {

    class Params(
        val name: String
    ) : UseCaseParams.Params()

    override fun execute(parameters: Params): Flow<List<OrgsUiModel>> {
        return detailsRepository.getOrganizations(parameters.name).map { result ->
            if (result is Result.Loading) {
                emptyList()
            } else {
                result.successOr(emptyList()).map { entity ->
                    OrgsUiModel(
                        id = entity.id,
                        description = entity.description,
                        name = entity.login
                    )
                }

            }
        }.distinctUntilChanged()
    }

    override fun mapExceptionToResult(params: Throwable): List<OrgsUiModel> {
        Logger.e(params.message)
        return emptyList()
    }
}



data class OrgsUiModel(
    val id: Int,
    val description: String?,
    val name: String?,
)