package dev.forcecodes.hov.domain.usecase.details

import dev.forcecodes.hov.core.Result
import dev.forcecodes.hov.core.internal.Logger
import dev.forcecodes.hov.core.model.empty
import dev.forcecodes.hov.core.qualifiers.IoDispatcher
import dev.forcecodes.hov.core.successOr
import dev.forcecodes.hov.domain.source.DetailsRepository
import dev.forcecodes.hov.domain.usecase.BaseFlowUseCase
import dev.forcecodes.hov.domain.usecase.UseCaseParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.json.JSONException
import org.json.JSONObject
import java.text.NumberFormat
import javax.inject.Inject
import javax.inject.Named

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
                result.data?.map { entity ->
                    OrgsUiModel(
                        id = entity.id,
                        description = entity.description,
                        name = entity.login
                    )
                } ?: emptyList()
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