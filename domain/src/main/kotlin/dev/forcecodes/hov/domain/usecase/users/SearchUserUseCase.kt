package dev.forcecodes.hov.domain.usecase.users

import dev.forcecodes.hov.core.Result
import dev.forcecodes.hov.core.error
import dev.forcecodes.hov.core.fold
import dev.forcecodes.hov.core.model.UserUiModel
import dev.forcecodes.hov.core.qualifiers.IoDispatcher
import dev.forcecodes.hov.core.successOr
import dev.forcecodes.hov.domain.source.DetailsRepository
import dev.forcecodes.hov.domain.usecase.FlowUseCase
import dev.forcecodes.hov.domain.usecase.UseCaseParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchUserUseCase @Inject constructor(
    private val detailsRepository: DetailsRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<SearchUserUseCase.SearchParams, List<UserUiModel>>(ioDispatcher) {

    class SearchParams(val name: String) : UseCaseParams.Params()

    override fun execute(parameters: SearchParams): Flow<Result<List<UserUiModel>>> {
        return detailsRepository.searchUser(parameters.name)
            .filterNot { it is Result.Loading }
            .map { result ->
            result.fold(onSuccess = {
                val filteredUsers = result.successOr(emptyList()).map { entity ->
                    UserUiModel.User(id = entity.id, name = entity.name)
                }
                Result.Success(filteredUsers)
            }, onFailure = {
                Result.Error(result.error)
            })
        }
    }
}