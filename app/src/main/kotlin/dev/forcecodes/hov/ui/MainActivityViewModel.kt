package dev.forcecodes.hov.ui

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forcecodes.hov.data.extensions.cancelWhenActive
import dev.forcecodes.hov.domain.usecase.users.ListItemUiState
import dev.forcecodes.hov.domain.usecase.users.RefreshUsersUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val refreshUsersUseCase: RefreshUsersUseCase
) : BaseViewModel<ListItemUiState, GithubUserUiEvent>(ListItemUiState.initial()) {

    private val _refreshState = MutableStateFlow(false)
    val refreshState: StateFlow<Boolean> = _refreshState

    override fun stateReducer(oldState: ListItemUiState, event: GithubUserUiEvent) {
        if (event is GithubUserUiEvent.OnLoad) {
            return
        }
        refreshJob?.cancelWhenActive()

        executeRefresh(oldState, event.loadPage(), event is GithubUserUiEvent.OnRefresh)
    }

    private var refreshJob: Job? = null

    private fun executeRefresh(oldState: ListItemUiState, page: Int, refresh: Boolean) {
        refreshJob = viewModelScope.launch {
            refreshUsersUseCase.invoke(RefreshUsersUseCase.Params(page)).collect { uiState ->
                setState(
                    oldState.copy(
                        hasItems = uiState.hasItems,
                        isLoading = uiState should refresh,
                        error = uiState.error
                    )
                )
                if (!refresh) {
                    return@collect
                }
                sideEffect(uiState)
            }
        }
    }

    private infix fun ListItemUiState.should(refresh: Boolean): Boolean =
        if (refresh) false else isLoading

    private fun sideEffect(uiState: ListItemUiState) {
        _refreshState.value = uiState.isLoading
    }

    private fun GithubUserUiEvent.loadPage(): Int {
        return if (this is GithubUserUiEvent.OnRefresh) page else 0
    }

    fun onRefresh(page: Int) {
        sendEvent(GithubUserUiEvent.OnRefresh(page))
    }

    fun onRetry() {
        sendEvent(GithubUserUiEvent.OnErrorRetry)
    }

    init {
        sendEvent(GithubUserUiEvent.Reload)
    }
}