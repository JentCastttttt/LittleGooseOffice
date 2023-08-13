package little.goose.note.ui.note

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.flow.collectLatest
import little.goose.common.constants.DEEP_LINK_THEME_AND_HOST
import little.goose.common.utils.log
import little.goose.note.data.constants.KEY_NOTE
import little.goose.note.data.constants.KEY_NOTE_ID
import little.goose.note.data.entities.NoteContentBlock

@Stable
sealed interface NoteScreenState {
    data object Loading : NoteScreenState

    data class State(val scaffoldState: NoteScaffoldState) : NoteScreenState
}

sealed class NoteScreenEvent {
    data class AddNoteBlock(val noteContentBlock: NoteContentBlock) : NoteScreenEvent()
}

sealed class NoteNavigatingType {
    data object Add : NoteNavigatingType()
    data class Edit(val noteId: Long) : NoteNavigatingType()
}

const val ROUTE_NOTE = "note"

private const val DEEP_LINK_URI_PATTERN_NOTE = "$DEEP_LINK_THEME_AND_HOST/$KEY_NOTE" +
        "/$KEY_NOTE_ID={$KEY_NOTE_ID}"

fun NavController.navigateToNote(
    type: NoteNavigatingType
) {
    val config: NavOptionsBuilder.() -> Unit = { launchSingleTop = true }
    when (type) {
        NoteNavigatingType.Add -> {
            navigate("$ROUTE_NOTE/-1", config)
        }

        is NoteNavigatingType.Edit -> {
            navigate("$ROUTE_NOTE/${type.noteId}", config)
        }
    }
}

fun NavGraphBuilder.noteRoute(onBack: () -> Unit) {
    composable(
        route = "$ROUTE_NOTE/{$KEY_NOTE_ID}",
        deepLinks = listOf(
            navDeepLink {
                uriPattern = DEEP_LINK_URI_PATTERN_NOTE
            }
        ),
        arguments = listOf(
            navArgument(KEY_NOTE_ID) {
                type = NavType.LongType
                defaultValue = -1L
            }
        )
    ) {
        NoteRoute(
            modifier = Modifier
                .fillMaxSize()
                .shadow(36.dp, clip = false),
            onBack = onBack
        )
    }
}

@Composable
fun NoteRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {

    val viewModel = hiltViewModel<NoteViewModel>()
    val noteRouteState by viewModel.noteScreenState.collectAsStateWithLifecycle()
    val blockColumnState = rememberLazyListState()

    NoteScreen(
        modifier = modifier,
        state = noteRouteState,
        blockColumnState = blockColumnState,
        onBack = onBack
    )

    LaunchedEffect(viewModel.noteScreenEvent) {
        viewModel.noteScreenEvent.collectLatest { event ->
            when (event) {
                is NoteScreenEvent.AddNoteBlock -> {
                    val noteContentState = (noteRouteState as? NoteScreenState.State)
                        ?.scaffoldState?.contentState ?: run {
                        return@collectLatest
                    }
                    // 定位新增的 Block
                    val blockIndex = noteContentState.content.indexOf(event.noteContentBlock)
                    // 等待重组完毕，FocusRequester 被添加到 Compose 树上
                    awaitFrame()
                    if (blockIndex != -1) {
                        // 标题占了一个位置，所以要 +1
                        blockColumnState.animateScrollToItem(blockIndex + 1, 12)
                    }
                    // 为新增的 Block 申请焦点
                    var tryTime = 0
                    val focusRequester = noteContentState.focusRequesters[event.noteContentBlock.id]
                        ?: return@collectLatest
                    do {
                        tryTime++
                        val result = runCatching {
                            focusRequester.requestFocus()
                        }.onFailure {
                            log(it)
                            awaitFrame()
                        }
                    } while (result.isFailure && tryTime < 10)
                }
            }
        }
    }
}