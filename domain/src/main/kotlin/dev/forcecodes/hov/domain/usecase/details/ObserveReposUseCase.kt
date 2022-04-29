package dev.forcecodes.hov.domain.usecase.details

import dev.forcecodes.hov.core.Result
import dev.forcecodes.hov.core.model.empty
import dev.forcecodes.hov.core.qualifiers.IoDispatcher
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
import javax.inject.Singleton

@Singleton
class ObserveReposUseCase @Inject constructor(
    @Named("jsonColor")
    private val jsonObject: JSONObject,
    private val detailsRepository: DetailsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseFlowUseCase<ObserveReposUseCase.Params, ReposViewState>(dispatcher) {

    private val numberFormatter = NumberFormat.getNumberInstance()

    class Params(
        val name: String
    ) : UseCaseParams.Params()

    override fun execute(parameters: Params): Flow<ReposViewState> {
        return detailsRepository.getRepositories(parameters.name).map { result ->
            when(result) {
                is Result.Success -> {
                    val data = result.data
                    ReposViewState(
                        false,
                        data.map { entity ->
                            UserRepoUiModel(
                                id = entity.id,
                                name = entity.name,
                                starred = formatStarredCount(entity.stargazersCount),
                                description = entity.description,
                                language = entity.language,
                                color = getColor(entity.language)
                            )
                        }
                    )
                }
                is Result.Error -> {
                    ReposViewState(false, emptyList(), result.exception.message)
                }
                is Result.Loading -> {
                    ReposViewState(
                        true,
                        result.data?.map { entity ->
                            UserRepoUiModel(
                                id = entity.id,
                                name = entity.name,
                                starred = formatStarredCount(entity.stargazersCount),
                                description = entity.description,
                                language = entity.language,
                                color = getColor(entity.language)
                            )
                        } ?: emptyList()
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

    override fun mapExceptionToResult(params: Throwable): ReposViewState {
        return ReposViewState(false, emptyList())
    }
}

data class ReposViewState(
    val isLoading: Boolean = true,
    val data: List<UserRepoUiModel> = emptyList(),
    val error: String? = null,
)

data class UserRepoUiModel(
    val id: Int,
    val name: String?,
    val starred: String,
    val description: String?,
    val language: String?,
    val color: String?
)

internal const val FALLBACK_COLOR = "#00FFFFFF"