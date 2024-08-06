package dev.forcecodes.android.gitprofile.ui.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint
import dev.forcecodes.android.gitprofile.R
import dev.forcecodes.gitprofile.core.internal.Logger
import dev.forcecodes.gitprofile.core.model.UserUiModel
import dev.forcecodes.android.gitprofile.ui.GithubUserViewModel
import dev.forcecodes.android.gitprofile.ui.MainActivityViewModel
import dev.forcecodes.android.gitprofile.ui.compose.theme.MyApplicationTheme
import dev.forcecodes.android.gitprofile.ui.compose.theme.Shapes
import dev.forcecodes.android.gitprofile.ui.compose.theme.Typography
import dev.forcecodes.android.gitprofile.ui.details.DetailsActivity
import dev.forcecodes.android.gitprofile.util.toUserContentUri

/**
 * Compose version of paginated Github users.
 *
 * **Note: SwipeRefresh and Paging** is currently not support due to limitations and some
 * potential flicker effect cause by recomposition when dealing with paginated data.
 */
@AndroidEntryPoint
class GithubUsersComposeFragment : Fragment() {

    private val activityViewModel by activityViewModels<MainActivityViewModel>()
    private val viewModel by viewModels<GithubUserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = requireContentView(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed) {
        MyApplicationTheme {
            GithubUserListScreen(viewModel, activityViewModel) { details ->
                startActivity(DetailsActivity.createIntent(requireContext(), details))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GithubUsersListScreenPreview() {
    MyApplicationTheme {
        LoadStateFooterScreen()
    }
}

@Composable
fun GithubUserListScreen(
    usersViewModel: GithubUserViewModel,
    activityViewModel: MainActivityViewModel,
    onClick: (Pair<Int, String>) -> Unit
) {

    val userList = usersViewModel.pagingFlow.collectAsLazyPagingItems()
    val refreshState = activityViewModel.refreshState.collectAsState()
    var pageIndex by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = pageIndex) {
        Logger.e("load more $pageIndex")
        usersViewModel.onLoadMore(pageIndex)
    }

    LazyColumn(
        contentPadding = PaddingValues(top = dimensionResource(id = R.dimen.spacing_small_2)),
        verticalArrangement = Arrangement.Top
    ) {
//        itemsIndexed(
//            userList,
//            key = { index, userUidModel ->
//                (userUidModel as UserUiModel.User).id
//            },
//            contentType = {
//
//            },
//            itemContent = {
//                index, userUiModel ->
//                GithubUser(userUiModel as? UserUiModel.User, onClick)
//            }
//        ) { index, userUiModel ->
//            GithubUser(userUiModel as? UserUiModel.User, onClick)
//            if (index == userList.itemSnapshotList.lastIndex) {
//                pageIndex = index
//            }
//        }

//        pagingLoadStateItem(
//            loadState = userList.loadState.source.append,
//            keySuffix = "append",
//            onError = {
//                LoadStateFooter()
//                // invalidate
//                userList.refresh()
//            }
//        )
    }
//    SwipeRefresh(
//        modifier = Modifier.fillMaxSize(),
//        state = rememberSwipeRefreshState(isRefreshing = refreshState.value),
//        onRefresh = { }
//    ) { }

}

// disable to prevent recomposition
fun LazyListScope.pagingLoadStateItem(
    loadState: LoadState,
    keySuffix: String? = null,
    onError: (@Composable LazyItemScope.(LoadState.NotLoading) -> Unit)? = null,
) {
    if (onError != null && loadState is LoadState.NotLoading && loadState.endOfPaginationReached) {
        item(
            key = keySuffix?.let { "footer" },
            content = { onError(loadState) },
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GithubUser(userUiModel: UserUiModel.User?, onClick: (Pair<Int, String>) -> Unit = {}) {

    val spacingNormal = dimensionResource(id = R.dimen.spacing_normal)
    val spacingSmall = dimensionResource(id = R.dimen.spacing_small_2)

    Card(
        modifier = Modifier
            .padding(
                start = spacingNormal,
                end = spacingNormal,
                bottom = spacingSmall
            )
            .fillMaxWidth(),
        shape = Shapes.large,
        onClick = {
            userUiModel?.let {
                onClick.invoke(Pair(it.id, it.name ?: return@let))
            }
        }
    ) {
        Row(
            modifier = Modifier.padding(spacingSmall)
        ) {
            GithubUserImage(userUiModel?.id.toString())
            Column(
                modifier = Modifier
                    // add 4dp margin to mimic 0.4 vertical bias of our XML version.
                    .padding(bottom = 4.dp)
                    .padding(start = spacingNormal)
                    .align(Alignment.CenterVertically),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = userUiModel?.name ?: "",
                    style = Typography.h6,
                    letterSpacing = 1.15.sp
                )
                Text(
                    text = "github.com/${userUiModel?.name}",
                    style = Typography.caption,
                    color = Color.Gray.copy(0.9f)
                )
            }
        }
    }
}

@Composable
fun GithubUserImage(imageUrl: String?) {
    AsyncImage(
        model = imageUrl?.toUserContentUri,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(dimensionResource(R.dimen.user_profile_size))
            .clip(Shapes.large)
    )
}