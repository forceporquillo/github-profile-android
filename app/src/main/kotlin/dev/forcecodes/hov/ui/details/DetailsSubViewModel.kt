package dev.forcecodes.hov.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forcecodes.hov.domain.usecase.details.DetailsViewState
import dev.forcecodes.hov.domain.usecase.details.ObserveOrgsUseCase
import dev.forcecodes.hov.domain.usecase.details.ObserveReposUseCase
import dev.forcecodes.hov.domain.usecase.details.ObserveStarredUseCase
import dev.forcecodes.hov.domain.usecase.details.OrgsUiModel
import dev.forcecodes.hov.domain.usecase.details.ReposViewState
import dev.forcecodes.hov.domain.usecase.details.StarredUiModel
import dev.forcecodes.hov.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsSubViewModel @Inject constructor(
    private val observeReposUseCase: ObserveReposUseCase,
    private val observeOrgsUseCase: ObserveOrgsUseCase,
    private val observeStarredUseCase: ObserveStarredUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<DetailsViewState, LoadUiActions>(DetailsViewState.initial()) {

    var name: String = ""
        get() = savedStateHandle.get<String>("details_name") ?: field
        private set

    private val _repositories = MutableStateFlow<ReposViewState>(ReposViewState())
    val repositories: StateFlow<ReposViewState> = _repositories

    private val _organizations = MutableStateFlow<List<OrgsUiModel>>(emptyList())
    val organizations: StateFlow<List<OrgsUiModel>> = _organizations

    private val _starredRepos = MutableStateFlow<List<StarredUiModel>>(emptyList())
    val starredRepos: StateFlow<List<StarredUiModel>> = _starredRepos

    private fun loadUserRepositories(name: String) {
        viewModelScope.launch {
            val params = ObserveReposUseCase.Params(name)
            observeReposUseCase.invoke(params).collect { uiModel ->
                _repositories.value = uiModel
            }
        }
    }

    private fun loadOrganizations(name: String) {
        viewModelScope.launch {
            val params = ObserveOrgsUseCase.Params(name)
            observeOrgsUseCase.invoke(params).collect { uiModel ->
                _organizations.value = uiModel
            }
        }
    }

    private fun loadStarredRepositories(name: String) {
        viewModelScope.launch {
            val params = ObserveStarredUseCase.Params(name)
            observeStarredUseCase.invoke(params).collect { uiModel ->
                _starredRepos.value = uiModel
            }
        }
    }

    private var currentItemPos: Int = 0

    fun currentItem(position: Int) {
        currentItemPos = position
    }

    override fun stateReducer(oldState: DetailsViewState, event: LoadUiActions) {
        when (event) {
            is LoadUiActions.LoadAll -> {
                // make copy for our refresh state
                this.name = event.name
                loadUserRepositories(event.name)
                loadOrganizations(event.name)
                loadStarredRepositories(event.name)
            }
            is LoadUiActions.Refresh -> {
                if (currentItemPos == 0) {
                    loadUserRepositories(name)
                }
                if (currentItemPos == 1) {
                    loadOrganizations(name)
                }
                if (currentItemPos == 2) {
                    loadStarredRepositories(name)
                }
            }
        }
    }

    override fun onCleared() {
        savedStateHandle["details_name"] = name
        super.onCleared()
    }
}