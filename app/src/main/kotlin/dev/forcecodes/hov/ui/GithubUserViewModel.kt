package dev.forcecodes.hov.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forcecodes.hov.core.internal.Logger
import dev.forcecodes.hov.core.model.UserUiModel
import dev.forcecodes.hov.core.successOr
import dev.forcecodes.hov.data.extensions.cancelWhenActive
import dev.forcecodes.hov.data.utils.NextPageIndexer
import dev.forcecodes.hov.domain.usecase.users.ListItemUiState
import dev.forcecodes.hov.domain.usecase.users.LoadMoreUsersUseCase
import dev.forcecodes.hov.domain.usecase.users.ObserveGithubUsersInteractor
import dev.forcecodes.hov.domain.usecase.users.SearchUserUseCase
import dev.forcecodes.hov.util.notNull
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubUserViewModel @Inject constructor(
    observeGithubUsersInteractor: ObserveGithubUsersInteractor,
    private val searchUserUseCase: SearchUserUseCase,
    private val nextPageIndexer: NextPageIndexer,
    private val loadMoreUsersUseCase: LoadMoreUsersUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<ListItemUiState, GithubUserUiEvent>(ListItemUiState.initial()) {

    private val lastIndexOrInitial: Int
        get() = savedStateHandle[LAST_PAGE_INDEX] ?: INITIAL_PAGE_INDEX

    private val _onLoadMore =
        MutableStateFlow(lastIndexOrInitial) // Channel<Int>(capacity = Channel.CONFLATED)
    private val loadMoreFlow: Flow<Int> = _onLoadMore

    val pagingFlow: Flow<PagingData<UserUiModel>> =
        observeGithubUsersInteractor.flow.cachedIn(viewModelScope)

    // side effect
    private val _loadMoreEffect = MutableStateFlow<LoadMoreEffect>(LoadMoreEffect.Nominal)
    val loadMoreEffect: StateFlow<LoadMoreEffect> = _loadMoreEffect

    private val onNextPage: (Int) -> Flow<ListItemUiState> = { nextId ->
        loadMoreUsersUseCase.invoke(LoadMoreUsersUseCase.Params(nextId))
    }

    private val _userSearchResults = MutableStateFlow<List<UserUiModel>>(emptyList())
    val userSearchResults: StateFlow<List<UserUiModel>> = _userSearchResults.asStateFlow()

    private var searchJob: Job? = null

    private suspend fun Flow<Int>.onLoadMoreItems(state: (ListItemUiState) -> Unit) {
        flatMapLatest(onNextPage::invoke).collectLatest(state)
    }

    init {
        viewModelScope.launch {
            loadMoreFlow.onLoadMoreItems(::setPagingSideEffect)
        }
        observeGithubUsersInteractor.invoke(ObserveGithubUsersInteractor.Params(PAGING_CONFIG))
    }

    override fun stateReducer(oldState: ListItemUiState, event: GithubUserUiEvent) {
        if (event is GithubUserUiEvent.OnLoad) {
            _onLoadMore.value = event.since
        }
        if (event is GithubUserUiEvent.OnSearchUser) {
            searchJob = executeSearchUser(event.name)
        }
    }

    private fun executeSearchUser(name: String) = viewModelScope.launch {
        flowOf(name)
            .filter { it.isNotEmpty() }
            .debounce(TIME_OUT_MILLIS)
            .flatMapMerge { query ->
                Logger.d("Query $query")
                searchUserUseCase.invoke(
                    SearchUserUseCase.SearchParams(query)
                )
            }.collect {
                Logger.d("Result $it")
                _userSearchResults.value = it.successOr(emptyList())
            }
    }

    private fun setPagingSideEffect(uiState: ListItemUiState) {
        _loadMoreEffect.value = if (uiState.error.notNull()
            && nextPageIndexer.takeMaxAndStore(-1) != 0
        ) {
            LoadMoreEffect.Error(uiState.error)
        } else {
            LoadMoreEffect.Nominal
        }
        setState(uiState)
    }

    fun onLoadMore(nextPage: Int) {
        val maxNextPage = nextPageIndexer.takeMaxAndStore(nextPage)
        sendEvent(GithubUserUiEvent.OnLoad(maxNextPage))
    }

    override fun onCleared() {
        savedStateHandle[LAST_PAGE_INDEX] = nextPageIndexer
            .takeMaxAndStore(INITIAL_PAGE_INDEX)
        super.onCleared()
    }

    fun searchUser(username: String) {
        searchJob?.cancelWhenActive()
        sendEvent(GithubUserUiEvent.OnSearchUser(username))
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 30,
            initialLoadSize = 60
        )
        private const val INITIAL_PAGE_INDEX = 0
        private const val TIME_OUT_MILLIS = 1000L
    }
}

private const val LAST_PAGE_INDEX = "last_search_index"