package dev.forcecodes.hov.ui

import androidx.lifecycle.ViewModel
import dev.forcecodes.hov.core.UiEvent
import dev.forcecodes.hov.core.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewModel<State : UiState, Event : UiEvent>(initialState: State) : ViewModel() {

    abstract fun stateReducer(oldState: State, event: Event)

    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state

    private val _uiEvent = Channel<Event>(capacity = Channel.CONFLATED)
    val uiEvent: Flow<Event> = _uiEvent.receiveAsFlow()

    fun sendEvent(event: Event) {
        _uiEvent.trySend(event)
        stateReducer(_state.value, event)
    }

    protected fun setState(newState: State) {
        _state.tryEmit(newState)
    }

    override fun onCleared() {
        super.onCleared()
        _uiEvent.close()
    }
}
