package dev.forcecodes.gitprofile.domain.usecase.users

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import dev.forcecodes.gitprofile.core.model.UserUiModel
import dev.forcecodes.gitprofile.data.cache.UserDao
import dev.forcecodes.gitprofile.data.cache.entity.UserEntity
import dev.forcecodes.gitprofile.domain.usecase.PagingInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveGithubUsersInteractor @Inject constructor(
    private val userDao: UserDao
) : PagingInteractor<ObserveGithubUsersInteractor.Params, UserUiModel, UserEntity>() {

    override fun createObservable(params: Params): Flow<PagingData<UserUiModel>> {
        return Pager(
            config = params.pagingConfig,
            pagingSourceFactory = { userDao.getPagingUsers() }
        ).flow.map { pagingData ->
            pagingData.map { userEntity -> userEntity.toUiModel() }
        }
    }

    override fun UserEntity.toUiModel(id: String?): UserUiModel {
        return UserUiModel.User(name = this.name, id = this.id)
    }

    data class Params(
        override val pagingConfig: PagingConfig
    ): PagingInteractor.Params<UserUiModel>

}