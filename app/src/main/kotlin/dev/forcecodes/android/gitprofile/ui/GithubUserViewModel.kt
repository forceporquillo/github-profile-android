package dev.forcecodes.android.gitprofile.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forcecodes.gitprofile.core.internal.Logger
import dev.forcecodes.gitprofile.core.model.UserUiModel
import dev.forcecodes.gitprofile.core.successOr
import dev.forcecodes.gitprofile.data.extensions.cancelWhenActive
import dev.forcecodes.gitprofile.data.utils.NextPageIndexer
import dev.forcecodes.gitprofile.domain.usecase.users.ListItemUiState
import dev.forcecodes.gitprofile.domain.usecase.users.LoadMoreUsersUseCase
import dev.forcecodes.gitprofile.domain.usecase.users.ObserveGithubUsersInteractor
import dev.forcecodes.gitprofile.domain.usecase.users.SearchUserUseCase
import dev.forcecodes.android.gitprofile.extensions.launchAndCollect
import dev.forcecodes.android.gitprofile.util.notNull
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        val params = LoadMoreUsersUseCase.Params(nextId)
        loadMoreUsersUseCase.invoke(params)
    }

    private val _userSearchResults = MutableStateFlow<List<UserUiModel>>(emptyList())
    val userSearchResults: StateFlow<List<UserUiModel>> = _userSearchResults.asStateFlow()

    private var searchJob: Job? = null

    private fun Flow<Int>.onLoadMoreItems(state: (ListItemUiState) -> Unit) {
        flatMapLatest(onNextPage::invoke).onEach { state.invoke(it) }.launchIn(viewModelScope)
    }

    init {
        loadMoreFlow.onLoadMoreItems(::setPagingSideEffect)
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

    private fun executeSearchUser(query: CharSequence): Job = launchAndCollect({
        val params = SearchUserUseCase.SearchParams(query.toString())
        searchUserUseCase.invoke(params)
    }) { uiResult ->
        _userSearchResults.value = uiResult.successOr(emptyList())
    }

    private fun setPagingSideEffect(uiState: ListItemUiState) {
        _loadMoreEffect.value =
            if (uiState.error.notNull() && nextPageIndexer.takeMaxAndStore(-1) != 0) {
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
        savedStateHandle[LAST_PAGE_INDEX] = nextPageIndexer.takeMaxAndStore(INITIAL_PAGE_INDEX)
        super.onCleared()
    }

    fun searchUser(query: CharSequence) {
        if (query.isEmpty()) {
            return
        }
        searchJob?.cancelWhenActive(CancellationException("Search query interrupted"))
        Logger.d("Query $query")
        sendEvent(GithubUserUiEvent.OnSearchUser(query))
    }

    fun clearSelections() {
        _userSearchResults.value = emptyList()
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 30, initialLoadSize = 60
        )
        private const val INITIAL_PAGE_INDEX = 0
        private const val TIME_OUT_MILLIS = 1000L
    }
}

private const val LAST_PAGE_INDEX = "last_search_index"