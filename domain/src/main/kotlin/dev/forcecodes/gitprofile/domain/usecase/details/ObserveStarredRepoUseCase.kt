package dev.forcecodes.gitprofile.domain.usecase.details

import dev.forcecodes.gitprofile.core.Result
import dev.forcecodes.gitprofile.core.model.empty
import dev.forcecodes.gitprofile.core.qualifiers.IoDispatcher
import dev.forcecodes.gitprofile.core.successOr
import dev.forcecodes.gitprofile.domain.source.DetailsRepository
import dev.forcecodes.gitprofile.domain.usecase.BaseFlowUseCase
import dev.forcecodes.gitprofile.domain.usecase.UseCaseParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.json.JSONException
import org.json.JSONObject
import java.text.NumberFormat
import javax.inject.Inject
import javax.inject.Named

class ObserveStarredUseCase @Inject constructor(
    @Named("jsonColor")
    private val jsonObject: JSONObject,
    private val detailsRepository: DetailsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseFlowUseCase<ObserveStarredUseCase.Params, List<StarredUiModel>>(dispatcher) {

    private val numberFormatter = NumberFormat.getNumberInstance()

    class Params(
        val name: String
    ) : UseCaseParams.Params()

    override fun execute(parameters: Params): Flow<List<StarredUiModel>> {
        return detailsRepository.getStarredRepositories(parameters.name).map { result ->
            if (result is Result.Loading) {
                result.data?.map { entity ->
                    StarredUiModel(
                        id = entity.id,
                        name = entity.owner?.login,
                        description = entity.description,
                        language = entity.language,
                        starredCount = formatStarredCount(entity.stargazersCount),
                        repoName = entity.name,
                        ownerId = entity.owner?.id,
                        color = getColor(entity.language)
                    )
                } ?: emptyList()
            } else {
                result.successOr(emptyList()).map { entity ->
                    StarredUiModel(
                        id = entity.id,
                        name = entity.owner?.login,
                        description = entity.description,
                        language = entity.language,
                        starredCount = formatStarredCount(entity.stargazersCount),
                        repoName = entity.name,
                        ownerId = entity.owner?.id,
                        color = getColor(entity.language)
                    )
                }
            }

        }.distinctUntilChanged()
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

    override fun mapExceptionToResult(params: Throwable): List<StarredUiModel> = emptyList()
}

data class StarredUiModel(
    val id: Int,
    val name: String?,
    val description: String?,
    val language: String?,
    val starredCount: String?,
    val repoName: String?,
    val ownerId: String?,
    val color: String?
)