package dev.forcecodes.android.gitprofile.ui.details

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forcecodes.gitprofile.data.extensions.cancelWhenActive
import dev.forcecodes.gitprofile.domain.usecase.details.DetailsViewState
import dev.forcecodes.gitprofile.domain.usecase.details.GetUserDetailsUseCase
import dev.forcecodes.android.gitprofile.ui.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    override fun stateReducer(oldState: DetailsViewState, event: DetailsUiEvent) {
        when (event) {
            is DetailsUiEvent.GetData -> {
                val id = event.pair.first
                val name = event.pair.second

                fetchDetailsJob?.cancelWhenActive()

                fetchDetailsJob = collectNewState(id, name) {details ->
                    if (details.error != null) {
                        delay(1250L)
                    }
                    val newState = oldState.copy(
                        details.isLoading,
                        details.error,
                        details.data
                    )
                    setSideEffects(newState)
                    setState(newState)
                }
            }
        }
    }

    private fun setSideEffects(state: DetailsViewState) {
        val errorMessage =
            if (!state.error.isNullOrEmpty() && state.data == null) state.error else null
        _finishWhenError.value = errorMessage
    }

    private fun collectNewState(id: Int, name: String, state: suspend (DetailsViewState) -> Unit) =
        viewModelScope.launch {
            getUserDetailsUseCase
                .invoke(GetUserDetailsUseCase.Params(id, name))
                .collect(state::invoke)
        }

    fun getDetails(pair: Pair<Int, String>) {
        sendEvent(DetailsUiEvent.GetData(pair))
    }

    override fun onCleared() {
        super.onCleared()
        fetchDetailsJob?.cancel()
    }
}