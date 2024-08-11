package dev.forcecodes.android.gitprofile.ui.details

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forcecodes.gitprofile.data.extensions.cancelWhenActive
import dev.forcecodes.gitprofile.domain.usecase.details.DetailsViewState
import dev.forcecodes.gitprofile.domain.usecase.details.GetUserDetailsUseCase
import dev.forcecodes.android.gitprofile.ui.BaseViewModel
import dev.forcecodes.gitprofile.domain.DataStrategy
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getUserDetailsUseCase: GetUserDetailsUseCase
) : BaseViewModel<DetailsViewState, DetailsUiEvent>(DetailsViewState.initial()) {

    private var fetchDetailsJob: Job? = null

    private val _finishWhenError = MutableStateFlow<String?>(null)
    val finishWhenError: StateFlow<String?> = _finishWhenError

    private var userExtras: Pair<Int, String> = Pair(0, "")

    override fun stateReducer(oldState: DetailsViewState, event: DetailsUiEvent) {
        if (event is DetailsUiEvent.GetData || event is DetailsUiEvent.Refresh) {
            fetchDetailsJob?.cancelWhenActive()
            fetchDetailsJob = collectNewState(
                id = event.data.first,
                name = event.data.second,
                strategy = event.strategy
            ) {
                val newState = oldState.copy(
                    isLoading = it.isLoading,
                    data = it.data,
                    error = it.error,
                    isForceRefresh = it.isForceRefresh
                )
                setSideEffects(newState)
                setState(newState)
            }
        }
    }

    private fun setSideEffects(state: DetailsViewState) {
        val errorMessage =
            if (!state.error.isNullOrEmpty() && state.data == null) state.error else null
        _finishWhenError.value = errorMessage
    }

    private fun collectNewState(id: Int, name: String, strategy: DataStrategy,
                                state: suspend (DetailsViewState) -> Unit) =
        viewModelScope.launch {
            getUserDetailsUseCase
                .invoke(GetUserDetailsUseCase.Params(id, name, strategy))
                .collect(state::invoke)
        }

    fun getDetails(pair: Pair<Int, String>) {
        userExtras = pair
        sendEvent(DetailsUiEvent.GetData(userExtras))
    }

    fun refresh() {
        sendEvent(DetailsUiEvent.Refresh(userExtras))
    }

    override fun onCleared() {
        super.onCleared()
        fetchDetailsJob?.cancel()
    }
}