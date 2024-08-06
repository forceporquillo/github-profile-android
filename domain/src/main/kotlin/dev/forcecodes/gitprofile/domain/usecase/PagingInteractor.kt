package dev.forcecodes.gitprofile.domain.usecase

import androidx.paging.PagingConfig
import androidx.paging.PagingData

/**
 * Domain interactor class for creating a paginated observables.
 */
abstract class PagingInteractor<P : PagingInteractor.Params<T>, T : Any, S> : SubjectInteractor<P, PagingData<T>>() {
    interface Params<T : Any> {
        val pagingConfig: PagingConfig
    }

    protected abstract fun S.toUiModel(id: String? = null): T
}