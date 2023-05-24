package little.goose.search.component

import android.view.ViewTreeObserver
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import little.goose.design.system.theme.AccountTheme

@Composable
internal fun SearchTopAppBar(
    modifier: Modifier = Modifier,
    keyword: String,
    onKeywordChange: (String) -> Unit,
    onBack: () -> Unit
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        title = {
            val focusRequester = remember { FocusRequester() }
            BasicTextField(
                value = keyword,
                onValueChange = onKeywordChange,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                maxLines = 1
            )
            val view = LocalView.current
            DisposableEffect(focusRequester) {
                val listener = object : ViewTreeObserver.OnWindowFocusChangeListener {
                    override fun onWindowFocusChanged(hasFocus: Boolean) {
                        if (hasFocus) {
                            focusRequester.requestFocus()
                            view.viewTreeObserver.removeOnWindowFocusChangeListener(this)
                        }
                    }
                }
                view.viewTreeObserver.addOnWindowFocusChangeListener(listener)
                onDispose {
                    view.viewTreeObserver.removeOnWindowFocusChangeListener(listener)
                }
            }
        },
        actions = {
            if (keyword.isNotEmpty()) {
                IconButton(onClick = { onKeywordChange("") }) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = "Clear"
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun PreviewSearchTopAppBar() = AccountTheme {
    SearchTopAppBar(
        keyword = "Search keyword",
        onKeywordChange = {},
        onBack = {}
    )
}